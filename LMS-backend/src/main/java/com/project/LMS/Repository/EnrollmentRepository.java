package com.project.LMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.LMS.model.Enrollment;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUser_userId(Long userId);

    Optional<Enrollment> findByUser_userIdAndCourse_courseId(Long userId, Long courseId);

    @Query(value =
            "SELECT * FROM (" +
                    "  SELECT u.name as userName, CAST(AVG(e.progress) AS DECIMAL(10,2)) as averageProgress " +
                    "  FROM enrollment e " +
                    "  JOIN users u ON e.user_id = u.user_id " +
                    "  GROUP BY u.name " +
                    "  ORDER BY AVG(e.progress) DESC" +
                    ") WHERE ROWNUM <= 4",
            nativeQuery = true)
    List<Object[]> getTopUsersByAverageProgress();
    @Query(value =
            "SELECT u.name as userName, u.email as userEmail, " +
                    "c.title as courseTitle, e.progress as progress " +
                    "FROM enrollment e " +
                    "JOIN users u ON e.user_id = u.user_id " +
                    "JOIN course c ON e.course_id = c.course_id " +
                    "WHERE u.role = 'Student' " +
                    "ORDER BY u.name, e.progress DESC",
            nativeQuery = true)
    List<Object[]> getAllUsersByAverageProgress();


}

