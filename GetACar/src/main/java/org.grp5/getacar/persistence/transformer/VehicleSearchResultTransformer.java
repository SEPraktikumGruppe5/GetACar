package org.grp5.getacar.persistence.transformer;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.grp5.getacar.persistence.dto.VehicleSearchResult;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.entity.VehicleImage;
import org.grp5.getacar.persistence.entity.VehicleType;
import org.hibernate.transform.BasicTransformerAdapter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
public class VehicleSearchResultTransformer extends BasicTransformerAdapter { // TODO: Write test for it!

    private final Function<String, VehicleImage> imageStringToVehicleImageFunction =
            new Function<String, VehicleImage>() {
                @Override
                public VehicleImage apply(String input) {
                    if (input == null) {
                        return null;
                    }
                    final VehicleImage vehicleImage = new VehicleImage();
                    final List<String> imageProperties = Lists.newArrayList(Splitter.on(";").split(input));
                    if (!imageProperties.isEmpty()) {
                        vehicleImage.setId(Integer.valueOf(imageProperties.get(0)));
                    }
                    if (imageProperties.size() > 1) {
                        vehicleImage.setFileName(imageProperties.get(1));
                    }
                    return vehicleImage;
                }
            };

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        final VehicleSearchResult vehicleSearchResult = new VehicleSearchResult();

        final Vehicle vehicle = new Vehicle();
        vehicle.setId((Integer) tuple[0]);
        vehicle.setActive((Boolean) tuple[1]);
        vehicle.setInitialLongitude((BigDecimal) tuple[2]);
        vehicle.setInitialLatitude((BigDecimal) tuple[3]);
        vehicle.setComment((String) tuple[4]);
        vehicle.setLicenseNumber((String) tuple[5]);

        // build and set vehicle images
        try {
            final String imagesString = new String((byte[]) tuple[6], "UTF-8"); // strange that 'group_concat' comes in as byte[]
            final List<String> imageStrings = Lists.newArrayList(Splitter.on(",").split(imagesString));
            final List<VehicleImage> vehicleImages = Lists.transform(imageStrings, imageStringToVehicleImageFunction);
            vehicle.getVehicleImages().addAll(vehicleImages);
            for (VehicleImage vehicleImage : vehicle.getVehicleImages()) {
                final Vehicle imageVehicle = new Vehicle(); // to prevent circular json encoding by jackson TODO: Can that be done better somehow?
                imageVehicle.setId(vehicle.getId());
                vehicleImage.setVehicle(imageVehicle);
            }
        } catch (UnsupportedEncodingException e) {
            // Snap, that really shouldn't happen!
        }

        final VehicleType vehicleType = new VehicleType();
        vehicleType.setId((Integer) tuple[7]);
        vehicleType.setName((String) tuple[8]);
        vehicleType.setIcon((String) tuple[9]);
        vehicleType.setDescription((String) tuple[10]);

        vehicle.setVehicleType(vehicleType);

        vehicleSearchResult.setVehicle(vehicle);

        vehicleSearchResult.setDistance((Double) tuple[11]);
        vehicleSearchResult.setCurrentLatitude((BigDecimal) tuple[12]);
        vehicleSearchResult.setCurrentLongitude((BigDecimal) tuple[13]);

        return vehicleSearchResult;
    }
}
