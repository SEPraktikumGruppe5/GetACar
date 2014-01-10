package org.grp5.getacar.persistence.dao;

import com.google.common.base.Preconditions;
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
                                          VehicleType vehicleType, DateTime startTime, DateTime endTime,
                                          DateTime atTime) {
        // TODO: Put into @NamedQuery?
        final String sql = "SELECT\n  final_result.f_id,\n  final_result.f_aktiv,\n  final_result.f_laengengrad_init,\n  " +
                "final_result.f_breitengrad_init,\n  final_result.f_bemerkung,\n  final_result.f_kennzeichen,\n  final_result.f_bilder,\n  " +
                "final_result.ft_id,\n  final_result.ft_name,\n  final_result.ft_icon,\n  final_result.ft_beschreibung,\n  " +
                "final_result.distance,\n  final_result.momentaner_breitengrad,\n  final_result.momentaner_laengengrad\n" +
                "FROM (SELECT\n        *,\n        (6371 * acos(cos(radians(tmp_result.momentaner_breitengrad)) " +
                "* cos(radians(:latitude)) *\n                     cos(radians(:longitude) - radians(tmp_result.momentaner_laengengrad)) " +
                "+\n                     sin(radians(tmp_result.momentaner_breitengrad)) * sin(radians(:latitude)))) AS distance\n      FROM " +
                "(SELECT\n              vehicle_and_type.f_id,\n              vehicle_and_type.f_aktiv,\n              vehicle_and_type.f_laengengrad_init,\n              " +
                "vehicle_and_type.f_breitengrad_init,\n              vehicle_and_type.f_bemerkung,\n              vehicle_and_type.f_kennzeichen,\n              group_concat(fb.fb_id, ';', fb.fb_dateiname) AS f_bilder,\n              " +
                "vehicle_and_type.ft_id,\n              vehicle_and_type.ft_name,\n              " +
                "vehicle_and_type.ft_icon,\n              vehicle_and_type.ft_beschreibung,\n              " +
                "CASE WHEN re.re_end_breitengrad IS NOT NULL THEN re.re_end_breitengrad\n              " +
                "ELSE vehicle_and_type.f_breitengrad_init END AS momentaner_breitengrad,\n              " +
                "CASE WHEN re.re_end_laengengrad IS NOT NULL THEN re.re_end_laengengrad\n              " +
                "ELSE vehicle_and_type.f_laengengrad_init END AS momentaner_laengengrad\n            FROM " +
                "(SELECT\n                    f.f_id,\n                    f.f_aktiv,\n                    f.f_laengengrad_init,\n                    f.f_breitengrad_init,\n                    f.f_bemerkung,\n                    " +
                "f.f_kennzeichen,\n                    f.ft_id,\n                    ft.ft_name,\n                    ft.ft_icon,\n                    " +
                "ft.ft_beschreibung\n                  FROM fahrzeug f, fahrzeugtyp ft\n                  WHERE f.f_aktiv = 1 AND CASE WHEN :vehicleTypeId IS NOT NULL THEN f.ft_id = :vehicleTypeId\n                  ELSE 1 = 1 END " +
                "AND\n                        ft.ft_id = f.ft_id) AS vehicle_and_type\n              LEFT OUTER JOIN reservierung re\n                ON re.re_id = " +
                "(SELECT\n                                 re_sub.re_id\n                               FROM reservierung re_sub\n                               WHERE re_sub.re_endzeit < :atTime " +
                "AND re_sub.f_id = vehicle_and_type.f_id\n                               ORDER BY re_sub.re_endzeit DESC\n                               LIMIT 1)\n              JOIN fahrzeugbild fb\n                ON fb.f_id = vehicle_and_type.f_id\n            GROUP BY vehicle_and_type.f_id\n           ) AS tmp_result) " +
                "AS final_result\nWHERE distance <= :radius AND ((SELECT\n                                  EXISTS(\n                                      SELECT\n                                        *\n                                      FROM reservierung ris\n                                      WHERE ris.f_id = final_result.f_id AND (\n                                        (ris.re_startzeit > :atTime)\n\n                                        OR\n                                        CASE WHEN (:startTime IS NOT NULL AND :endTime IS NOT NULL) THEN\n                                          (\n                                            (ris.re_startzeit > :startTime AND ris.re_startzeit < :endTime)\n                                            OR (ris.re_startzeit = :startTime AND ris.re_endzeit <= :endTime)\n                                            OR (ris.re_startzeit <= :startTime AND ris.re_endzeit >= :endTime)\n                                            OR ((ris.re_startzeit = :startTime) AND (ris.re_endzeit = :endTime))\n                                            OR (ris.re_endzeit > :startTime AND ris.re_endzeit < :endTime)\n                                            OR ((ris.re_startzeit < :startTime) AND (ris.re_endzeit > :endTime))\n                                          )\n                                        ELSE 1 = 0 END\n                                      )\n                                      LIMIT 1\n                                  )) = 0)\nORDER BY distance ASC;";

        final Session hibernateSession = getHibernateSession();
        SQLQuery query = hibernateSession.createSQLQuery(sql);
        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);
        query.setParameter("radius", radius);
        query.setParameter("vehicleTypeId", (vehicleType != null ? vehicleType.getId() : null));
        query.setParameter("startTime", (startTime != null ? startTime.toString() : null)); // when passing in the objects, the checks did not work! Remember endTime always pass them as strings!
        query.setParameter("endTime", (endTime != null ? endTime.toString() : null));
        query.setParameter("atTime", Preconditions.checkNotNull(atTime.toString()));
        query.setResultTransformer(new VehicleSearchResultTransformer());
        return query.list();
    }
}