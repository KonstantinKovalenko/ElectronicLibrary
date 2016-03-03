package logic.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("IOMySQL")
public class IOMySQL {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addAccount(String userName, String password, boolean adminRights) {
        final int id_user = jdbcTemplate.queryForInt("select max(ID_User) from accounts") + 1;
        jdbcTemplate.update("insert into accounts (ID_User, UserName, UserPassword, AdminRights) values (" + id_user + ",'" + userName + "','" + password + "'," + adminRights + ")");
    }

    public void addBook(Book book) {
        final int id_book = jdbcTemplate.queryForInt("select max(ID_Book) from books") + 1;
        jdbcTemplate.update("insert into books (ID_Book, Name, Author, PublishingHouse, Description, PicturePath, BookPath) "
                + "values ("
                + id_book + ",'"
                + book.getName() + "','"
                + book.getAuthor() + "','"
                + book.getPublishingHouse() + "','"
                + book.getDescription() + "','"
                + book.getPicturePath() + "','"
                + book.getBookPath() + "')");
    }

    public void updatePicturePath(String name, String picturePath) {
        int bookId = getBookIDByName(name);
        jdbcTemplate.update("update books set PicturePath = \"" + picturePath + "\" where ID_Book = '" + bookId + "'");
    }

    public boolean validateUserName(String userName) {
        List<String> userNames = jdbcTemplate.queryForList("select UserName from accounts", String.class);
        for (String s : userNames) {
            if (s.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public boolean validateUserPassword(String userName, String password) {
        Object obj = jdbcTemplate.queryForObject("select UserPassword from accounts where UserName = '" + userName + "'", String.class);
        return password.equals((String) obj);
    }

    public int getBookIDByName(String bookName) {
        if (bookIsExistByName(bookName)) {
            return jdbcTemplate.queryForInt("select ID_Book from books where Name = '" + bookName + "'");
        }
        return 0;
    }

    public int getBookIDByDescription(String description) {
        String bookDescription = getBookDescriptionIfExist(description);
        if (!bookDescription.isEmpty()) {
            return jdbcTemplate.queryForInt("select ID_Book from books where Description = '" + bookDescription + "'");
        }
        return 0;
    }

    private String getBookDescriptionIfExist(String description) {
        List<String> listDescriptions = jdbcTemplate.queryForList("select Description from books", String.class);
        String pattern = "";
        String workDescription = description;
        if (description.contains(" ")) {
            pattern = workDescription.substring(0, workDescription.indexOf(" ")) + " ";
            workDescription = workDescription.substring(workDescription.indexOf(" ") + 1);
            if (workDescription.contains(" ")) {
                pattern += workDescription.substring(0, workDescription.indexOf(" "));
            }
            pattern += workDescription;
        } else {
            pattern = description;
        }
        for (String s : listDescriptions) {
            s = s.toLowerCase();
            if (s.contains(pattern.toLowerCase())) {
                return s;
            }
        }
        return "";
    }

    private boolean bookIsExistByName(String bookName) {
        List<String> listNames;
        listNames = jdbcTemplate.queryForList("select Name from books", String.class);
        for (String s : listNames) {
            s = s.toLowerCase();
            if (s.equals(bookName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        int booksAmount = jdbcTemplate.queryForInt("select max(ID_Book) from books") + 1;
        for (int i = 1; i < booksAmount; i++) {
            books.add(getBook(i));
        }
        return books;
    }

    public Book getBook(int bookId) {
        Book book = new Book();
        book.setId(bookId);
        book.setName(getBookName(bookId));
        book.setAuthor(getBookAuthor(bookId));
        book.setPublishingHouse(getBookPubHouse(bookId));
        book.setDescription(getBookDescription(bookId));
        book.setPicturePath(getBookPicPath(bookId));
        book.setBookPath(getBookPath(bookId));
        return book;
    }

    private String getBookName(int bookId) {
        String result;
        Object obj = jdbcTemplate.queryForObject("select Name from books where ID_Book = '" + bookId + "'", String.class);
        result = (String) obj;
        return result;
    }

    private String getBookAuthor(int bookId) {
        String result;
        Object obj = jdbcTemplate.queryForObject("select Author from books where ID_Book = '" + bookId + "'", String.class);
        result = (String) obj;
        return result;
    }

    private String getBookPubHouse(int bookId) {
        String result;
        Object obj = jdbcTemplate.queryForObject("select PublishingHouse from books where ID_Book = '" + bookId + "'", String.class);
        result = (String) obj;
        return result;
    }

    private String getBookDescription(int bookId) {
        String result;
        Object obj = jdbcTemplate.queryForObject("select Description from books where ID_Book = '" + bookId + "'", String.class);
        result = (String) obj;
        return result;
    }

    private String getBookPicPath(int bookId) {
        String result;
        Object obj = jdbcTemplate.queryForObject("select PicturePath from books where ID_Book = '" + bookId + "'", String.class);
        /*File file = new File((String) obj);
         result = file.getAbsolutePath();*/
        result = (String) obj;
        return result;
    }

    private String getBookPath(int bookId) {
        String result;
        Object obj = jdbcTemplate.queryForObject("select BookPath from books where ID_Book = '" + bookId + "'", String.class);
        File file = new File((String) obj);
        result = file.getAbsolutePath();
        return result;
    }

    public List<String> getCommentsByID(int bookId) {
        List<String> result = jdbcTemplate.queryForList("select Commentary from comments where ID_Book = '" + bookId + "'", String.class);
        return result;
    }

    public List<String> getUsersByID(int bookId) {
        List<String> result = jdbcTemplate.queryForList("select User from comments where ID_Book = '" + bookId + "'", String.class);
        return result;
    }

    public void addComment(int bookId, String userName, String commentary) {
        jdbcTemplate.update("insert into comments (ID_Book, User, Commentary) values (" + bookId + ",'" + userName + "','" + commentary + "')");
    }

    public boolean isAdmin(String userName) {
        final int admin = jdbcTemplate.queryForInt("select AdminRights from accounts where UserName = '" + userName + "'");
        return admin == 1;
    }
}
