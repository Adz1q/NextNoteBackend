package org.adz1q.nextnote.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String password;

    //Zrobić autoryzację - działa ale przy dużych literach pokazuję, że się nie zgadza
    //Naprawić wyszukiwanie notatek po id użytkownika
}
