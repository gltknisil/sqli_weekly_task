package com.example.vulnerabledemo;

//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;  
/*
jakarta.persistence.* satırları (3 tanesi) silindi 
çünkü artık ham SQL sorgusu yazmıyoruz, 
bu araçlara ihtiyacımız yok. 
java.util.Optional eklendi 
çünkü yeni findByEmailAndPassword metodumuz
 Optional<Student> döndürüyor. */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController // bu sınıf internetten gelen http isteklerini karşılayacak
class VulnerableAuthController {

    // @PersistenceContext
    // private EntityManager entityManager;
    /*
     * Fark: EntityManager (ham SQL çalıştırma aracı) yerine,
     * doğrudan StudentRepository'yi (hazır metodlu repository)
     * kullanıyoruz.
     */
    @Autowired
    private StudentRepository studentRepository;

    // --- 1. ZAFİYET: LOGIN BYPASS (SQLi) ---
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        // ⚠️ KASITLI OLARAK SAVUNMASIZ: string birleştirme kullanıyor

        /*
         * String sql = "SELECT * FROM student WHERE email = '" + email +
         * "' AND password = '" + password + "'";
         * 
         * Query query = entityManager.createNativeQuery(sql, Student.class);
         * List<Student> result = query.getResultList();
         * 
         * if (!result.isEmpty()) {
         * return ResponseEntity.ok("Giriş başarılı: " + result.get(0).getFullName());
         * }
         * return ResponseEntity.status(401).body("Giriş başarısız");
         */
        Optional<Student> result = studentRepository.findByEmailAndPassword(email, password);

        if (result.isPresent()) {
            return ResponseEntity.ok("Giriş başarılı: " + result.get().getFullName());
        }
        return ResponseEntity.status(401).body("Giriş başarısız");
    }

    // --- 2. ZAFİYET: ARAMA / UNION-BASED SQLi ---
    @GetMapping("/search")
    public ResponseEntity<?> searchStudents(@RequestParam String name) {
        // ⚠️ KASITLI OLARAK SAVUNMASIZ: string birleştirme kullanıyor
        /*
         * String sql = "SELECT * FROM student WHERE full_name LIKE '%" + name + "%'";
         * 
         * Query query = entityManager.createNativeQuery(sql, Student.class);
         * List<Student> result = query.getResultList();
         * 
         * return ResponseEntity.ok(result);
         */
        List<Student> result = studentRepository.findByFullNameContaining(name);
        return ResponseEntity.ok(result);
    }
}

@Component
class DataSeeder implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... args) {
        studentRepository.save(new Student("ahmet@okul.edu.tr", "sifre123", "Ahmet Yılmaz"));
        studentRepository.save(new Student("ayse@okul.edu.tr", "guvenli456", "Ayşe Demir"));
    }
}