package logic.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.database.Book;
import logic.database.IOMySQL;
import logic.database.User;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

@Service("UserService")
public class UserService {

    @Autowired
    IOMySQL mySql;

    public String buildBooksList() {
        String result = "";
        ArrayList<Book> books = mySql.getAllBooks();
        for (Book b : books) {
            result += b.getAuthor() + " - " + b.getName() + "<br/>";
        }
        return result;
    }

    public String buildBookLink(Book book) {
        return book.getAuthor() + " - <a href = \"bookPage.htm\">" + book.getName() + "</a><br/>";
    }

    public void fillPageModel(ModelMap model, Book book, User user) {
        model.addAttribute("adminRights", user.isAdminRights());
        model.addAttribute("userName", user.getUserName());
        model.addAttribute("bookName", book.getName());
        model.addAttribute("bookAuthor", book.getAuthor());
        model.addAttribute("bookPublishingHouse", book.getPublishingHouse());
        model.addAttribute("bookDescription", book.getDescription());
        model.addAttribute("bookPicturePath", book.getPicturePath());
        model.addAttribute("bookPath", book.getBookPath());
    }

    public void buildComments(ModelMap model, Book book) {
        ArrayList<String> usersAndComments = new ArrayList<>();

        Object[] arrComments = mySql.getCommentsByID(book.getId()).toArray();
        Object[] arrCommentUsers = mySql.getUsersByID(book.getId()).toArray();

        for (int i = 0; i < arrComments.length; i++) {
            usersAndComments.add((String) arrCommentUsers[i] + ": " + (String) arrComments[i]);
        }
        model.addAttribute("usersAndComments", usersAndComments);
    }

    public void buildCommentsWithUserComment(ModelMap model, User user, Book book, String comment) {
        mySql.addComment(book.getId(), user.getUserName(), comment);

        ArrayList<String> usersAndComments = new ArrayList<>();

        Object[] arrComments = mySql.getCommentsByID(book.getId()).toArray();
        Object[] arrCommentUsers = mySql.getUsersByID(book.getId()).toArray();

        for (int i = 0; i < arrComments.length; i++) {
            usersAndComments.add((String) arrCommentUsers[i] + ": " + (String) arrComments[i]);
        }
        model.addAttribute("usersAndComments", usersAndComments);
    }

    public void downloadFile(Book book, HttpServletRequest request, HttpServletResponse response) {
        ServletContext context = request.getServletContext();
        File downloadFile = new File(book.getBookPath());
        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            inputStream = new FileInputStream(downloadFile);
            response.setContentLength((int) downloadFile.length());
            response.setContentType(context.getMimeType(book.getBookPath()));

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            outStream = response.getOutputStream();
            IOUtils.copy(inputStream, outStream);
        } catch (Exception e) {
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != inputStream) {
                    outStream.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public boolean isFileHaveWrongExtention(String fileName) {
        String fileExtention = fileName.substring(fileName.lastIndexOf(".") + 1);
        return !fileExtention.equals("txt")
                && !fileExtention.equals("rtf")
                && !fileExtention.equals("doc")
                && !fileExtention.equals("odt")
                && !fileExtention.equals("pdf");
    }

    public void fillBook(Book resultBook, Book uploadedBook) {
        resultBook.setName(uploadedBook.getName());
        resultBook.setAuthor(uploadedBook.getAuthor());
        resultBook.setPublishingHouse(uploadedBook.getPublishingHouse());
        resultBook.setDescription(uploadedBook.getDescription());
    }

    public void makeDirectoriesOnHDD(Book book) {
        String rootPath = "C:/ElectronicLibrary" + "/" + book.getAuthor();
        File dir = new File(rootPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void uploadFile(File uploadFile, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadFile))) {
            stream.write(bytes);
            stream.flush();
        }
    }

    public void updatePicturePathInDB(Book book, String fileName) {
        String bookPath = "C:/ElectronicLibrary" + "/" + book.getAuthor() + "/" + fileName;
        mySql.updatePicturePath(book.getName(), bookPath);
    }

    public void addBookToDB(Book book, String fileName) {
        book.setBookPath("C:/ElectronicLibrary" + "/" + book.getAuthor() + "/" + fileName);
        mySql.addBook(book);
    }

    public Book getBookFromDBByName(String bookName) {
        int bookId = mySql.getBookIDByName(bookName);
        Book result = mySql.getBook(bookId);
        return result;
    }

    public Book getBookFromDBByDescription(String description) {
        int bookId = mySql.getBookIDByDescription(description);
        Book result = mySql.getBook(bookId);
        return result;
    }

    public boolean bookIsNotExistByName(String bookName) {
        int bookId = mySql.getBookIDByName(bookName);
        return bookId == 0;
    }

    public boolean bookIsExistByName(String bookName) {
        return !bookIsNotExistByName(bookName);
    }

    public boolean bookIsExistByDescription(String description) {
        return !bookIsNotExistByDescription(description);
    }

    private boolean bookIsNotExistByDescription(String description) {
        int bookId = mySql.getBookIDByDescription(description);
        return bookId == 0;
    }
}
