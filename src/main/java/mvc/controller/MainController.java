package mvc.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.Configuration;
import logic.database.Book;
import logic.database.UploadedFile;
import logic.database.User;
import logic.service.AccountService;
import logic.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class MainController {

    private User cUser;
    private Book book = new Book();
    private final ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
    private final AccountService accountService = (AccountService) context.getBean("AccountService");
    private final UserService userService = (UserService) context.getBean("UserService");
    private final String REDIRECT_USER_INDEX = "redirect: /userIndex.htm";
    private final String REDIRECT_ADMIN_INDEX = "redirect: /adminIndex.htm";
    private final String REDIRECT_REGISTER = "redirect: /register.htm";

    @RequestMapping(value = "/index.htm")
    public String index(ModelMap model) {
        model.addAttribute("message", "Приветствую, дорогой библиофил!");
        return "index";
    }

    @RequestMapping("/login.htm")
    public String login(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "password", required = false) String password) {
        return "login";
    }

    @RequestMapping("/loginProcess.htm")
    public String loginProcess(@ModelAttribute("currentUser") User currentUser) {
        if (accountService.accountIsNotValid(currentUser)) {
            return "redirect: /login.htm";
        }
        cUser = currentUser;
        if (accountService.accountIsAdmin(currentUser)) {
            cUser.setAdminRights(true);
            return REDIRECT_ADMIN_INDEX;
        }
        return REDIRECT_USER_INDEX;
    }

    @RequestMapping("/register.htm")
    public String register(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "password", required = false) String password) {
        return "register";
    }

    @RequestMapping("/registerProcess.htm")
    public String registerProcess(@ModelAttribute("currentUser") User currentUser) {
        if (accountService.passwordIsEmpty(currentUser)) {
            return REDIRECT_REGISTER;
        }
        if (accountService.userNameIsExist(currentUser)) {
            return REDIRECT_REGISTER;
        }
        cUser = currentUser;
        accountService.addUserToMySQL(cUser);
        return REDIRECT_USER_INDEX;
    }

    @RequestMapping("/adminIndex.htm")
    public String adminIndex(@RequestParam(value = "search", required = false, defaultValue = "") String search, ModelMap model) {
        if (accountService.isNotAdmin(cUser)) {
            return REDIRECT_USER_INDEX;
        }
        model.addAttribute("userName", cUser.getUserName());
        if (search.isEmpty()) {
            model.addAttribute("headerMessage", "Список всех доступных книг:");
            model.addAttribute("books", userService.buildBooksList());
            return "adminIndex";
        }
        if (userService.bookIsExistByName(search)) {
            book = userService.getBookFromDBByName(search);
            model.addAttribute("headerMessage", "Найденные книги:");
            model.addAttribute("book", userService.buildBookLink(book));
            return "adminIndex";
        }
        if (userService.bookIsExistByDescription(search)) {
            book = userService.getBookFromDBByDescription(search);
            model.addAttribute("headerMessage", "Найденные книги:");
            model.addAttribute("book", userService.buildBookLink(book));
            return "adminIndex";
        }
        model.addAttribute("headerMessage", "Поиск результатов не дал.");
        return "adminIndex";
    }

    @RequestMapping("/userIndex.htm")
    public String userIndex(@RequestParam(value = "search", required = false, defaultValue = "") String search, ModelMap model) {
        model.addAttribute("userName", cUser.getUserName());
        if (search.isEmpty()) {
            model.addAttribute("headerMessage", "Список всех доступных книг:");
            model.addAttribute("books", userService.buildBooksList());
            return "userIndex";
        }
        if (userService.bookIsExistByName(search)) {
            book = userService.getBookFromDBByName(search);
            model.addAttribute("headerMessage", "Найденные книги:");
            model.addAttribute("book", userService.buildBookLink(book));
            return "userIndex";
        }
        if (userService.bookIsExistByDescription(search)) {
            book = userService.getBookFromDBByDescription(search);
            model.addAttribute("headerMessage", "Найденные книги:");
            model.addAttribute("book", userService.buildBookLink(book));
            return "userIndex";
        }
        model.addAttribute("headerMessage", "Поиск результатов не дал.");
        return "userIndex";
    }

    @RequestMapping("/bookPage.htm")
    public String bookPage(@RequestParam(value = "addComment", required = false, defaultValue = "") String addComment, ModelMap model) {
        userService.fillPageModel(model, book, cUser);
        if (addComment.isEmpty()) {
            userService.buildComments(model, book);
            return "bookPage";
        }
        userService.buildCommentsWithUserComment(model, cUser, book, addComment);
        return "bookPage";
    }

    @RequestMapping(value = "/downloadBook.dl")
    public @ResponseBody
    void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        userService.downloadFile(book, request, response);
    }

    @RequestMapping("/addBook.htm")
    public ModelAndView addBook(
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "author", defaultValue = "", required = false) String author,
            @RequestParam(value = "publishingHouse", defaultValue = "", required = false) String publishingHouse,
            @RequestParam(value = "description", defaultValue = "", required = false) String description) {
        return new ModelAndView("addBook");
    }

    @RequestMapping(value = "/successUploaded.htm")
    public ModelAndView uploadFile(
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
            @ModelAttribute("uploadedBook") Book uploadedBook,
            ModelMap model) {
        book = new Book();
        MultipartFile file = uploadedFile.getFile();
        String uploadError = "";
        String fileName = file.getOriginalFilename();
        model.addAttribute("failed", true);
        if (file.isEmpty()) {
            uploadError = "Ошибка: Файл пуст";
            return new ModelAndView("successUploaded", "message", uploadError);
        }
        if (userService.isFileHaveWrongExtention(fileName)) {
            uploadError = "Ошибка: Неправильный формат книги";
            return new ModelAndView("successUploaded", "message", uploadError);
        }
        if (userService.bookIsExistByName(uploadedBook.getName())) {
            uploadError = "Ошибка: Книга с таким именем уже существует";
            return new ModelAndView("successUploaded", "message", uploadError);
        }
        model.addAttribute("failed", false);
        userService.fillBook(book, uploadedBook);
        userService.makeDirectoriesOnHDD(book);
        userService.addBookToDB(book, fileName);
        try {
            String rootPath = "C:/ElectronicLibrary" + "/" + book.getAuthor();
            File uplFile = new File(rootPath + File.separator + fileName);
            userService.uploadFile(uplFile, file);
            return new ModelAndView("successUploaded", "message", fileName + " успешно загружен!");
        } catch (Exception e) {
            uploadError = "Ошибка при загрузке файла";
            model.addAttribute("failed", true);
            return new ModelAndView("successUploaded", "message", uploadError);
        }
    }

    @RequestMapping("/addPicture.htm")
    public ModelAndView addPicture(
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
            @RequestParam(value = "name", defaultValue = "", required = false) String name) {
        return new ModelAndView("addPicture");
    }

    @RequestMapping("/successUploadedPicture.htm")
    public ModelAndView uploadPicture(@ModelAttribute("uploadedFile") UploadedFile uploadedFile, @ModelAttribute("uploadedBook") Book uploadedBook, ModelMap model) {
        MultipartFile file = uploadedFile.getFile();
        String uploadError = "";
        String fileName = file.getOriginalFilename();
        if (file.isEmpty()) {
            model.addAttribute("failed", true);
            uploadError = "Ошибка: Файл пуст";
            return new ModelAndView("successUploadedPicture", "message", uploadError);
        }
        model.addAttribute("failed", false);
        Book cBook = userService.getBookFromDBByName(uploadedBook.getName());
        userService.updatePicturePathInDB(cBook, fileName);
        try {
            String rootPath = "C:/ElectronicLibrary" + "/" + cBook.getAuthor();
            File uplFile = new File(rootPath + File.separator + fileName);
            userService.uploadFile(uplFile, file);
            return new ModelAndView("successUploadedPicture", "message", fileName + " успешно загружен!");
        } catch (Exception e) {
            uploadError = "Ошибка при загрузке файла";
            model.addAttribute("failed", true);
            return new ModelAndView("successUploadedPicture", "message", uploadError);
        }
    }

    @RequestMapping(value = "/img.jpg")
    public ResponseEntity<byte[]> loadImage() throws IOException {
        final org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        File image = new File(book.getPicturePath());
        byte[] content = FileUtils.readFileToByteArray(image);
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
