package org.grp5.getacar.web.servlet;

import com.google.inject.Inject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.grp5.getacar.web.guice.annotation.AbsoluteImagePath;
import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class ImageUploadServlet extends HttpServlet {

    private final String absoluteImagePath;
    private File fileUploadPath;

    @Inject
    public ImageUploadServlet(@AbsoluteImagePath String absoluteImagePath) {
        this.absoluteImagePath = absoluteImagePath;
    }

    @Override
    public void init(ServletConfig config) {
        fileUploadPath = new File(absoluteImagePath);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("getfile") != null
                && !request.getParameter("getfile").isEmpty()) {
            File file = new File(fileUploadPath,
                    request.getParameter("getfile"));
            if (file.exists()) {
                int bytes = 0;
                ServletOutputStream op = response.getOutputStream();

                response.setContentType(getMimeType(file));
                response.setContentLength((int) file.length());
                response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

                byte[] bbuf = new byte[1024];
                DataInputStream in = new DataInputStream(new FileInputStream(file));

                while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
                    op.write(bbuf, 0, bytes);
                }

                in.close();
                op.flush();
                op.close();
            }
        } else if (request.getParameter("delfile") != null && !request.getParameter("delfile").isEmpty()) {
            File file = new File(fileUploadPath, request.getParameter("delfile"));
            if (file.exists()) {
                file.delete(); // TODO: Check and report success
            }
        } else if (request.getParameter("getthumb") != null && !request.getParameter("getthumb").isEmpty()) {
            File file = new File(fileUploadPath, request.getParameter("getthumb"));
            if (file.exists()) {
                String mimetype = getMimeType(file);
                if (mimetype.endsWith("png") || mimetype.endsWith("jpeg") || mimetype.endsWith("gif")) {
                    BufferedImage im = ImageIO.read(file);
                    if (im != null) {
                        BufferedImage thumb = Scalr.resize(im, 75);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        if (mimetype.endsWith("png")) {
                            ImageIO.write(thumb, "PNG", os);
                            response.setContentType("image/png");
                        } else if (mimetype.endsWith("jpeg") || mimetype.endsWith("jpg")) {
                            ImageIO.write(thumb, "jpg", os);
                            response.setContentType("image/jpeg");
                        } else {
                            ImageIO.write(thumb, "GIF", os);
                            response.setContentType("image/gif");
                        }
                        ServletOutputStream srvos = response.getOutputStream();
                        response.setContentLength(os.size());
                        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
                        os.writeTo(srvos);
                        srvos.flush();
                        srvos.close();
                    }
                }
            } // TODO: check and report success
        } else {
            PrintWriter writer = response.getWriter();
            writer.write("call POST with multipart form data");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }

        ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        JSONArray json = new JSONArray();
        try {
            List<FileItem> items = uploadHandler.parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    File file = new File(fileUploadPath, item.getName());
                    item.write(file);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", item.getName());
                    jsonObject.put("size", item.getSize());
                    jsonObject.put("url", "http://localhost:8080/getacar/uploads/?getfile=" + item.getName()); // TODO: Relative!
                    jsonObject.put("thumbnail_url", "http://localhost:8080/getacar/uploads/?getthumb=" + item.getName());
                    jsonObject.put("deleteUrl", "http://localhost:8080/getacar/uploads/?delfile=" + item.getName());
                    jsonObject.put("deleteType", "GET");
                    json.put(jsonObject);
                }
            }
            /*{"files": [
                {
                    "url": "http://jquery-file-upload.appspot.com/AMIfv94ocfe8DhQr4zlQmN3-oC6DN3T9lwSrUAGoQkibN3vNbtdYklu1KLTnDt7IuxwV9BB2YK-eZgy97CLRVwMZNuqo6UuXPRY6AjHbfOC6e4KgVCM5_HAHdDg4GSvwmmWBAp8CUq8l-A-UzsEIsrjUZrUXtjwmsyMkRIpWffV70lsFwzeWkvU/DSC04774.JPG",
                    "thumbnailUrl": "http://lh4.ggpht.com/Mbd5yRaadEEfAidABlcRm1atbPcXqISlhIjl2hQcemEu8B3yYNXo8cdmD7QFrNUyEU0jXh56m5sDZCEgaB0DLpC--ytmvcY=s80",
                    "name": "DSC04774.JPG",
                    "type": "image/jpeg",
                    "size": 257742,
                    "deleteUrl": "http://jquery-file-upload.appspot.com/AMIfv94ocfe8DhQr4zlQmN3-oC6DN3T9lwSrUAGoQkibN3vNbtdYklu1KLTnDt7IuxwV9BB2YK-eZgy97CLRVwMZNuqo6UuXPRY6AjHbfOC6e4KgVCM5_HAHdDg4GSvwmmWBAp8CUq8l-A-UzsEIsrjUZrUXtjwmsyMkRIpWffV70lsFwzeWkvU/DSC04774.JPG?delete=true",
                    "deleteType": "DELETE"
                }
            ]}*/
        } catch (FileUploadException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            writer.write(json.toString());
            writer.close();
        }

    }

    private String getMimeType(File file) throws IOException {
        String mimetype = "";
        if (file.exists()) {
            mimetype = Files.probeContentType(file.toPath());
        }
        System.out.println("mimetype: " + mimetype);
        return mimetype;
    }


    private String getSuffix(String filename) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            suffix = filename.substring(pos + 1);
        }
        System.out.println("suffix: " + suffix);
        return suffix;
    }
}