package com.denemesite.proje2.controller;

import com.denemesite.proje2.entity.Books;
import com.denemesite.proje2.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final BooksRepository booksRepository;

    @Autowired
    public HomeController(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("books", booksRepository.findAll());
        return "home";  // home.html dosyasını döndür
    }

    @PostMapping("/submit-text")
    public String submitText(@RequestParam("userText") String userText, Model model) {
        if (userText == null || userText.trim().isEmpty()) {
            // Boş metin gönderildiğinde hata mesajı
            model.addAttribute("message", "Hata: Gönderilen metin boş olamaz.");
        } else {
            // Veri tabanına kaydetme
            Books book = new Books();
            book.setName(userText);
            booksRepository.save(book);
            model.addAttribute("message", "Metin başarıyla veri tabanına kaydedildi.");
        }
        model.addAttribute("books", booksRepository.findAll());  // Güncel listeyi tekrar db ye ekler
        return "home";
    }

    // Silme işlemi
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, Model model) {
        Books book = booksRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz kitap id: " + id));
        booksRepository.delete(book);  // Kaydı sil
        model.addAttribute("message", "Kayıt başarıyla silindi.");
        model.addAttribute("books", booksRepository.findAll());  //  Güncel listeyi tekrar db ye ekler
        return "redirect:/";
    }
}
