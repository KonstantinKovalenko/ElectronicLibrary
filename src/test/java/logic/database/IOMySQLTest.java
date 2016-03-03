package logic.database;

import java.util.ArrayList;
import logic.Configuration;
import logic.service.UserService;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IOMySQLTest {

    private IOMySQL mySql;
    private UserService userService;

    @Before
    public void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        mySql = (IOMySQL) context.getBean("IOMySQL");
        userService = (UserService) context.getBean("UserService");
    }

    @Test
    @Ignore
    public void validateUserName() {
        if (mySql.validateUserName("user")) {
            assertTrue(mySql.validateUserPassword("user", "user"));
        }
    }

    @Test
    @Ignore
    public void validateIsAdmin() {
        assertTrue(mySql.isAdmin("admin"));
    }

    @Test
    @Ignore
    public void insertUserName() {
        mySql.addAccount("admin2", "admin2", true);
    }

    @Test
    @Ignore
    public void testGetBook() {
        System.out.println(mySql.getBook(1));
    }

    @Test
    @Ignore
    public void testGetAllBooks() {
        ArrayList<Book> books = mySql.getAllBooks();
        for (Book b : books) {
            System.out.println(b.toString());
        }
    }

    @Test
    @Ignore
    public void testUpdatePath() {
        mySql.updatePicturePath("Срыв", "D:/Downloads/Книги, Манга, Мануалы/Прочитано/Дмитрий Рус/Играть чтобы жить(закончено)/02Клан.fb2");
    }

    @Test
    @Ignore
    public void testGetBookIdByDescription() {
        int id = mySql.getBookIDByDescription("по искоренению");
        System.out.println(id);
    }

    @Test
    public void testGetBookFromDBByDescription() {
        Book book = userService.getBookFromDBByDescription("Род гэллоуглас");
        System.out.println(book);
    }
}
