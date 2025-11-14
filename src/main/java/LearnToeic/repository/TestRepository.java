package LearnToeic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import LearnToeic.entity.Test;

public interface TestRepository extends JpaRepository<Test, Integer> {
    //List<Test>findAllByOrderByTestDateDesc(); //lấy hết danh sách
    // Phân trang tất cả đề (nếu cần)
    Page<Test> findAllByOrderByTestDateDesc(Pageable pageable);

    // Search theo tên đề (có chứa keyword, không phân biệt hoa thường)
    Page<Test> findByTestNameContainingIgnoreCaseOrderByTestDateDesc(String keyword, Pageable pageable);
}
