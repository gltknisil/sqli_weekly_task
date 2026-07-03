//package com.example.vulnerabledemo;

//import org.springframework.data.jpa.repository.JpaRepository;

//public interface StudentRepository extends JpaRepository<Student, Long> {
//}
// spring data jpa kütüphanesinin methodlarını kullanyoruz

package com.example.vulnerabledemo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Güvenli login sorgusu — Spring Data JPA otomatik olarak
    // parametreli (prepared statement) SQL üretir
    Optional<Student> findByEmailAndPassword(String email, String password);

    // Güvenli arama sorgusu — isim içinde geçen kelimeyi arar
    List<Student> findByFullNameContaining(String name);
}

// iki yeni method ekledik ve şöyle okunuyor
// SELECT * FROM student WHERE email = ? AND password = ?
// SELECT * FROM student WHERE full_name LIKE %?% mantık aynı fakat
// artık parametreli üretiyor

/*
 * 1-)Spring, interface'i tarıyor, JpaRepository'yi extend ettiğini görüyor
 * 2-)findByEmailAndPassword gibi ekstra metod isimlerini fark ediyor
 * 3-)Bu isimleri parse ediyor
 * (findBy + Email + And + Password şeklinde parçalara ayırıyor)
 * 4-)Her parçayı Student sınıfındaki
 * (@Entity ile işaretli) alan isimleriyle eşleştiriyor
 * 5-)Bu eşleşmeden bir SQL sorgusu üretiyor
 * 6-)Runtime'da, bu interface'in gerçek bir implementasyonunu
 * (bir proxy sınıf) dinamik olarak oluşturuyor,
 * 
 * @Autowired ile sizin AuthController'ınıza o proxy'yi veriyor
 */