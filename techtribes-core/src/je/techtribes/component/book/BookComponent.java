package je.techtribes.component.book;

import com.structurizr.annotation.Component;
import je.techtribes.domain.Book;

import java.util.List;

@Component(description = "Provides access to information about books by local authors.")
public interface BookComponent {

    /**
     * Gets a list of books by local authors.
     */
    List<Book> getBooks();

}
