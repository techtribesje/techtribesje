package je.techtribes.component.book;

import com.structurizr.annotation.ContainerDependency;
import je.techtribes.component.contentsource.ContentSourceComponent;
import je.techtribes.domain.Book;
import je.techtribes.domain.ContentSource;
import je.techtribes.util.AbstractComponent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ContainerDependency(target="Relational Database", description = "Reads from and writes to")
class BookComponentImpl extends AbstractComponent implements BookComponent {

    private ContentSourceComponent contentSourceComponent;

    BookComponentImpl(ContentSourceComponent contentSourceComponent) {
        this.contentSourceComponent = contentSourceComponent;
    }

    @Override
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();

        try {
            books.add(createBook(1,
                    "Professional JSP 2nd Edition",
                    "Java has fimly established itself as the major enterprise development platform and has been widely adopted by many corporations around the world. Wrox's commitment to the Java programming community continues with timely updates to its core technology libary with Professional Java Server Programming J2EE 1.3 Edition and Professional JSP 2nd Edition and builds on this solid base with more focused drill down titles on topics such as WebObjects and Web design.",
                    "http://www.amazon.com/Professional-JSP-2nd-Simon-Brown/dp/1861004958/",
                    "simonbrown",
                    "Wrox",
                    "April 2001",
                    Book.Role.Coauthor));

            books.add(createBook(2,
                    "Professional Java Servlets",
                    "Java servlets are fast becoming indispensable enterprise components, as they provide a means to build scalable and portable business services that communicate over the Web. This book provides a comprehensive guide to developing applications based on the Java Servlet 2.3 API, a part of the Java 2 Platform, Enterprise Edition 1.3. The book reveals how and where servlets fit into an enterprise solution, and addresses issues such as security, scalability, performance and design. It walks you through the API covering the role of all the classes and interfaces and provides lots of example applications to demonstrate servlets in action. The book also covers the key role that servlets play in the new web services development model.",
                    "http://www.amazon.com/Professional-Java-Servlets-Andrew-Harbourne-Thomas/dp/186100561X",
                    "simonbrown",
                    "Wrox",
                    "January 2002",
                    Book.Role.Coauthor));

            books.add(createBook(3,
                    "Professional JSP Tag Libraries",
                    "JSP Tag Libraries is an extension to Java Server Pages, the java API for web development, that allows a cleaner separation of logic and presentation. This promotes genuine reusability and improves the scope and power of JSP. JSP Tag Libraries teaches you how to create usable, maintainable, and flexible tags. We will teach you good practices, and the design implications of tags, that will enable you to maximise the reusability of your code. This book includes many useful code examples that illustrate the points being made. With the prevalence of material on using of jsp tags, we will not concentrate in detail on using 3rd party tags, but rather aim to take you from writing script-like JSP based applications to creating genuinely object oriented web applications.",
                    "http://www.amazon.com/Professional-JSP-Libraries-Simon-Brown/dp/1861006217",
                    "simonbrown",
                    "Wrox",
                    "April 2002",
                    Book.Role.Author));

            books.add(createBook(4,
                    "Pro JSP 3rd Edition",
                    "Simpler, faster, easier dynamic website development based on new additions to an established and proven technologythat's what JavaServer Pages (JSP) 2.0 is all about. Pro JSP, Third Edition is the most comprehensive guide and reference to JSP 2.0 yet. It equips you with the tools, techniques, and understanding you need to develop web applications with JSP and Java servlets.",
                    "http://www.apress.com/java/jsp/9781590592250",
                    "simonbrown",
                    "Apress",
                    "September 2003",
                    Book.Role.Coauthor));

            books.add(createBook(5,
                    "Pro JSP 2",
                    "This is the first comprehensive guide to cover JSP 2 and 2.1. It supplies you with the tools and techniques to develop web applications with JSP and Java servlets. You’ll learn to choose and implement the best persistence option for your web applications, and how to secure web sites against malicious attack and accidental misuse. You will improve the performance and scalability of JSP pages, as well as architect reliable, stable applications. The authors describe all of the rich JSP 2 features, and explain JSF integration with JSP. Completing the thorough package, this book examines how integration with open source projects like Ant, Struts, XDoclet, JUnit, and Cactus can make web development even easier.",
                    "http://www.apress.com/java/jsp/9781590595138",
                    "simonbrown",
                    "Apress",
                    "December 2005",
                    Book.Role.Coauthor));

            books.add(createBook(6,
                    "Software Architecture for Developers",
                    "Technical leadership by coding, coaching, collaboration, architecture sketching and just enough up front design",
                    "https://leanpub.com/software-architecture-for-developers",
                    "simonbrown",
                    "Leanpub",
                    "February 2012",
                    Book.Role.Author));

            books.add(createBook(7,
                    "Are you an IT project manager?",
                    "Projects, programmes, governance, reporting, risks, issues, assumptions and dependencies.",
                    "https://leanpub.com/projectmanager",
                    "kirstiebrown",
                    "Leanpub",
                    "March 2013",
                    Book.Role.Author));

            books.add(createBook(8,
                    "Practical SQL Server Performance Tuning",
                    "Are your Stored Procedures sluggish? Is locking and blocking driving you to distraction? Are you cursing your cursors? Then join me for a trip through the highs and lows of performance tuning MS SQL Server!",
                    "https://leanpub.com/practicalperformancetuningforsqlserver",
                    "mattchatterley",
                    "Leanpub",
                    null,
                    Book.Role.Author));

            books.add(createBook(10,
                    "Talking with Tech Leads",
                    "A book for Tech Leads, from Tech Leads. Discover how more than 35 Tech Leads find the delicate balance between the technical and non-technical worlds. Discover the challenges a Tech Lead faces and how to overcome them. You may be surprised by the lessons they have to share.",
                    "https://leanpub.com/talking-with-tech-leads",
                    "simonbrown",
                    "Leanpub",
                    "September 2014",
                    Book.Role.Contributor));

            books.add(createBook(11,
                    "Microsoft System Center 2012 Compliance Management Cookbook",
                    "Over 40 practical recipes that will help you plan, build, implement, and enhance IT compliance policies using Microsoft Security Compliance Manager and Microsoft System Center 2012 R2",
                    "https://www.packtpub.com/virtualization-and-cloud/microsoft-system-center-2012-compliance-management-cookbook",
                    "ronnieisherwood",
                    "Packt",
                    "October 2014",
                    Book.Role.Coauthor));

            books.add(createBook(9,
                    "Professional Java EE Design Patterns",
                    "Master Java EE design pattern implementation to improve your design skills and your application’s architecture",
                    "http://www.wrox.com/WileyCDA/WroxTitle/Professional-Java-EE-Design-Patterns.productCd-111884341X.html",
                    "alextheedom",
                    "Wrox",
                    "December 2014",
                    Book.Role.Coauthor));

        } catch (Exception e) {
            logError(e.getMessage());
            throw new BookException("Error getting list of books", e);
        }

        Collections.reverse(books);
        return books;
    }

    @Override
    public List<Book> getBooks(ContentSource contentSource) {
        if (contentSource == null) {
            return new ArrayList<>();
        }

        List<Book> books = getBooks();
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : books) {
            if (contentSource.equals(book.getContentSource())) {
                filteredBooks.add(book);
            }
        }

        return filteredBooks;
    }

    private Book createBook(int id, String title, String description, String url, String author, String publisher, String publishedDate, Book.Role role) throws Exception {
        Book book = new Book(id);
        book.setTitle(title);
        book.setDescription(description);
        book.setUrl(new URL(url));
        book.setContentSource(contentSourceComponent.findByShortName(author));
        book.setPublisher(publisher);
        book.setPublishedDate(publishedDate);
        book.setRole(role);

        return book;
    }

}