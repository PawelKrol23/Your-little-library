package com.example.Spring6webapp.models.author;

import com.example.Spring6webapp.models.book.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, max = 70, message = "First names length must be between {min} and {max}")
    private String firstName;
    @Size(min = 2, max = 70, message = "Last names length must be between {min} and {max}")
    private String lastName;
    @NotNull(message = "Date of birth cannot be empty")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @NotNull(message = "You need to choose a nationality")
    private Nationality nationality;
    @ManyToMany(mappedBy = "authors", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    @Builder.Default
    private Set<Book> books = new HashSet<>();

    @PreRemove
    private void removeBookAssociations() {
        for (Book book: this.books) {
            book.getAuthors().remove(this);
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Author author = (Author) o;
        return getId() != null && Objects.equals(getId(), author.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
