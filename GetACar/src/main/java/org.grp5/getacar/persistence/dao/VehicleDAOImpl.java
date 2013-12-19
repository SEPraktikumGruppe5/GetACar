package org.grp5.getacar.persistence.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.grp5.getacar.persistence.dto.VehicleSearchResult;
import org.grp5.getacar.persistence.entity.Vehicle;
import org.grp5.getacar.persistence.entity.VehicleType;
import org.grp5.getacar.persistence.transformer.VehicleSearchResultTransformer;
import org.grp5.getacar.persistence.validation.ValidationHelper;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Access Object for {@link org.grp5.getacar.persistence.entity.Vehicle} entities.
 */
public class VehicleDAOImpl extends BaseDAOImpl<Integer, Vehicle> implements VehicleDAO {

    @Inject
    public VehicleDAOImpl(ValidationHelper validationHelper, Provider<EntityManager> entityManagerProvider,
                          Provider<Session> sessionProvider) {
        super(validationHelper, entityManagerProvider, sessionProvider);
    }

    @Override
    public List<VehicleSearchResult> find(BigDecimal latitude, BigDecimal longitude, Integer radius,
                                          VehicleType vehicleType, DateTime from, DateTime to, DateTime atTime) {
        // TODO: Put into @NamedQuery?
        // TODO: Use from / to in query in 'LEFT OUTER JOIN reservierung' part to not get already rented cars!
        final String sql = "SELECT final_result.f_id, final_result.f_aktiv, final_result.f_laengengrad, " +
                "final_result.f_breitengrad, final_result.f_bemerkung, final_result.f_kennzeichen, " +
                "final_result.f_bild, final_result.ft_id, final_result.ft_name, final_result.ft_beschreibung, " +
                "final_result.radius FROM (SELECT *, (6371 * acos(cos(radians(tmp_result.momentaner_breitengrad)) " +
                "* cos(radians(:latitude)) * cos(radians(:longitude) - radians(tmp_result.momentaner_laengengrad)) " +
                "+ sin(radians(tmp_result.momentaner_breitengrad)) * sin(radians(:latitude)))) AS radius FROM " +
                "(SELECT vehicle_and_type.f_id, vehicle_and_type.f_aktiv, vehicle_and_type.f_laengengrad, " +
                "vehicle_and_type.f_breitengrad, vehicle_and_type.f_bemerkung, vehicle_and_type.f_kennzeichen, " +
                "vehicle_and_type.f_bild, vehicle_and_type.ft_id, vehicle_and_type.ft_name, " +
                "vehicle_and_type.ft_beschreibung, " +
                "CASE WHEN re.re_end_breitengrad IS NOT NULL THEN re.re_end_breitengrad " +
                "ELSE vehicle_and_type.f_breitengrad END AS momentaner_breitengrad, " +
                "CASE WHEN re.re_end_laengengrad IS NOT NULL THEN re.re_end_laengengrad " +
                "ELSE vehicle_and_type.f_laengengrad END AS momentaner_laengengrad FROM " +
                "(SELECT f.f_id, f.f_aktiv, f.f_laengengrad, f.f_breitengrad, f.f_bemerkung, " +
                "f.f_kennzeichen, f.f_bild, f.ft_id, ft.ft_name, ft.ft_beschreibung FROM fahrzeug f, fahrzeugtyp ft " +
                "WHERE f.f_aktiv = 1 AND f.ft_id = :vehicleTypeId AND ft.ft_id = f.ft_id) AS vehicle_and_type " +
                "LEFT OUTER JOIN reservierung re ON re.re_id = (SELECT re_sub.re_id FROM reservierung re_sub " +
                "WHERE re_sub.re_endzeit < :atTime AND re_sub.f_id = vehicle_and_type.f_id " +
                "ORDER BY re_sub.re_endzeit DESC LIMIT 1)) AS tmp_result) AS final_result WHERE radius <= :radius " +
                "ORDER BY radius ASC;";

        final Session hibernateSession = getHibernateSession();
        SQLQuery query = hibernateSession.createSQLQuery(sql);
        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);
        query.setParameter("radius", radius);
        query.setParameter("vehicleTypeId", vehicleType.getId());
        query.setParameter("atTime", atTime);
        // TODO: from / to parameter
        query.setResultTransformer(new VehicleSearchResultTransformer());
        return query.list();
    }
}