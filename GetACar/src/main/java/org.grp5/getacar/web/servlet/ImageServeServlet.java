package org.grp5.getacar.web.servlet;

import com.google.inject.Inject;
import org.grp5.getacar.web.guice.annotation.AbsoluteImagePath;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

import static com.google.common.io.Files.newInputStreamSupplier;

/**
 *
 */
public class ImageServeServlet extends HttpServlet {

    private final String absoluteImagePath;

    private static final String IMAGE = "image";

    @Inject
    public ImageServeServlet(@AbsoluteImagePath String absoluteImagePath) {
        this.absoluteImagePath = absoluteImagePath;
    }
//    private static final String CONTENT_TYPE = "application/octet-stream";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.processRequest(request, response);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String imageFileName = request.getParameter(IMAGE);
        if (imageFileName == null || imageFileName.isEmpty()) {
            throw new IOException("Could not find file: No image-parameter given");
        }
        File file = new File(absoluteImagePath + imageFileName);
        if (!file.exists()) {
            throw new IOException("Image can not be found");
        }
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType(Files.probeContentType(file.toPath()));
        response.setContentLength((int) file.length());
//        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());

        byte[] buffer = new byte[8 * 1024]; // 8k buffer
        FileInputStream inputStream = newInputStreamSupplier(file).getInput();
        new DataInputStream(new FileInputStream(file));
        int length = 0;
        try {
            while ((inputStream != null) && ((length = inputStream.read(buffer)) != -1)) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            response.setContentType("text/plain");
            response.getOutputStream().print(stringWriter.toString());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            outputStream.flush();
            outputStream.close();
        }
    }
}