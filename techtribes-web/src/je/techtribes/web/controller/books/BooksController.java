package je.techtribes.web.controller.books;

import com.structurizr.annotation.Component;
import com.structurizr.annotation.UsedBy;
import je.techtribes.component.book.BookComponent;
import je.techtribes.domain.Book;
import je.techtribes.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@Component(description = "Allows users to view books by local authors.")
@UsedBy( person = "Anonymous User", description = "uses" )
public class BooksController extends AbstractController {

    private BookComponent bookComponent;

    @Autowired
    public BooksController(BookComponent bookComponent) {
        this.bookComponent = bookComponent;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
	public String viewRecentNews(ModelMap model) {
        List<Book> books = bookComponent.getBooks();

        model.addAttribute("books", books);
        addCommonAttributes(model);
        setPageTitle(model, "Books");

        return "books";
	}

}
