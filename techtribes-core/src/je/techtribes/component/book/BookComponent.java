package je.techtribes.component.book;

import com.structurizr.annotation.Component;
import je.techtribes.domain.Book;
import je.techtribes.domain.ContentSource;

import java.util.List;

/**
 * Provides access to information about books by local authors.
 */
@Component
public interface BookComponent {

    /**
     * Gets a list of books by local authors.
     */
    List<Book> getBooks();

    /**
     * Gets a list of books for a specific author.
     */
    List<Book> getBooks(ContentSource contentSource);

}
