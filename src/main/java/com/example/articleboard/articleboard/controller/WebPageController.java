package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.Post;
import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.exception.custom.PostCouldNotBeFound;
import com.example.articleboard.articleboard.repository.DealerAgentRepository;
import com.example.articleboard.articleboard.repository.PostRepository;
import com.example.articleboard.articleboard.repository.UserRepository;
import com.example.articleboard.articleboard.security.jwt.JwtProvider;
import com.example.articleboard.articleboard.service.AdminService;
import com.example.articleboard.articleboard.service.DealerAgentService;
import com.example.articleboard.articleboard.service.PostService;
import com.example.articleboard.articleboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WebPageController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private DealerAgentRepository dealerAgentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DealerAgentService dealerAgentService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/admin/usermanage/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = adminService.getUserById(id);
        model.addAttribute("user", user);
        return "th/New/NewAdminUpdateUser";
    }

    @GetMapping("/admin")
    public String adminPage(Model model,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "3") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userList = userService.getUserList(pageable);
        model.addAttribute("users", userList);
        return "th/New/NewAdminPage";
    }

    @GetMapping("/admin/")
    public String userPage(Model model, @RequestParam("pageNo") Integer pageNo) {
        int size = 3;
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<User> userList = userService.getUserList(pageable);
        model.addAttribute("users", userList);

        return "th/New/NewAdminPage";
    }

    @GetMapping("/admin/search")
    public String searchUsers(@RequestParam("username") String searchKeyword, Model model,
                              @RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "3") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userList = userRepository.findByUsernameContaining(searchKeyword, pageable);
        model.addAttribute("users", userList);
        return "th/New/NewAdminPage";
    }

    @GetMapping("/user/login")
    public String loginPage() {
        return "th/Turbo/turboLogin";
    }

    @GetMapping("/membership/DealerAgent/edit/{id}")
    public String editDealer(@PathVariable long id, Model model)
    {
        DealerAgent dealerAgent = dealerAgentRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("dealerAgent", dealerAgentService.getDealerAgent(id));
        model.addAttribute("postId", dealerAgentRepository.findById(id));

        return "th/Turbo/turboDealerAgentUpdate";
    }

    @GetMapping("/membership/DealerAgent/insert")
    public String dealerAgentInsert() {
        return "th/Turbo/turboDealerAgentInsert";
    }

    @GetMapping("/membership/DealerAgent/{id}")
    public String  getDealerAgent(@PathVariable long id, Model model)
    {
        DealerAgent dealerAgent = dealerAgentRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("dealerAgent", dealerAgentService.getDealerAgent(id));

        String nickName = dealerAgent.getUser().getNickName();
        model.addAttribute("nickName", nickName);

        return "th/Turbo/turboDealerAgentDetail";
    }

    @GetMapping("/membership/DealerAgent/more")
    public ResponseEntity<Map<String, Object>> dealerAgentMore(@RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<DealerAgent> dealerAgentList = dealerAgentService.getDealerAgentList(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", dealerAgentList.getContent());
        response.put("last", dealerAgentList.isLast());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/membership/DealerAgent")
    public String DealerAgentPage(Model model,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<DealerAgent> dealerAgentList = dealerAgentService.getDealerAgentList(pageable);
        long count = dealerAgentService.countDealerAgent();
        model.addAttribute("totalPosts", count);
        model.addAttribute("posts", dealerAgentList);
        return "th/Turbo/turboDealerAgent";
    }

    @GetMapping("/membership/DealerAgent/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchDealerAgents(@RequestParam("category") String category,
                                                           @RequestParam("keyword") String keyword,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<DealerAgent> dealerAgentList;
        if (keyword == null || keyword.isEmpty()) {
            dealerAgentList = dealerAgentRepository.findAll(pageable);    // 검색어 입력 안하믄 전체 출력
        } else {

            switch (category) {
                case "title":
                    dealerAgentList = dealerAgentRepository.findByTitleContaining(keyword, pageable);
                    break;
                case "author":
                    dealerAgentList = dealerAgentRepository.findByUser_NickNameContaining(keyword, pageable);
                    break;
                case "all":
                    dealerAgentList = dealerAgentRepository.findByTitleContainingOrUser_NickNameContaining(keyword, keyword, pageable);
                    break;
                default:
                    dealerAgentList = dealerAgentRepository.findAll(pageable);
                    break;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("content", dealerAgentList.getContent());
        response.put("last", dealerAgentList.isLast());
        response.put("totalPosts", dealerAgentList.getTotalElements());

        return ResponseEntity.ok(response);
    }
        @GetMapping("/user/signUp")
    public String signUpPage() {
        // return "th/signup";
        return "th/Turbo/TurboSignup";
    }

    @GetMapping("/contact/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchPosts(@RequestParam("category") String category,
                                                           @RequestParam("keyword") String keyword,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> postList;
        if (keyword == null || keyword.isEmpty()) {
            postList = postRepository.findAll(pageable);    // 검색어 입력 안하믄 전체 출력
        } else {

            switch (category) {
                case "title":
                    postList = postRepository.findByTitleContaining(keyword, pageable);
                    break;
                case "author":
                    postList = postRepository.findByUser_NickNameContaining(keyword, pageable);
                    break;
                case "all":
                    postList = postRepository.findByTitleContainingOrUser_NickNameContaining(keyword, keyword, pageable);
                    break;
                default:
                    postList = postRepository.findAll(pageable);
                    break;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", postList.getContent());
        response.put("last", postList.isLast());
        response.put("totalPosts", postList.getTotalElements());

        return ResponseEntity.ok(response);
    }

    // title 오름차순 정렬 검색 api(로 바뀔 예정)
    @GetMapping("/post/search")
    public String searchPosts(@RequestParam("title") String searchKeyword, Model model,
                              @RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "5") Integer size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postList = postRepository.findByTitleContaining(searchKeyword, pageable);
        model.addAttribute("posts", postList);
        return "th/New/NewIndex";
    }

    @GetMapping("/post/")
    public String postPage(Model model, @RequestParam("pageNo") Integer pageNo) {
        int size = 3;
        Pageable pageable = PageRequest.of(pageNo, size);
        Page<Post> postList = postService.getPostList(pageable);
        model.addAttribute("posts", postList);

        return "th/New/NewIndex";
    }

    @GetMapping("/contact")
    public String contactPage(Model model,
                              @RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        /*Pageable pageable = PageRequest.of(page, size);*/
        Page<Post> postList = postService.getPostList(pageable);
        long count = postService.countPost();
        model.addAttribute("totalPosts", count);
        model.addAttribute("posts", postList);

        return "th/Turbo/turboContact";
    }

    @GetMapping("/contact/more")
    public ResponseEntity<Map<String, Object>> contactMore(@RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> postList = postService.getPostList(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", postList.getContent());
        response.put("last", postList.isLast());

        return ResponseEntity.ok(response);
    }
    /*@GetMapping("/")
    public String indexPage(Model model,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "3") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postList = postService.getPostList(pageable);
        model.addAttribute("posts", postList);

        return "th/New/NewIndex";
    }*/
    @GetMapping("/")
    public String indexPage() {
        return "th/Turbo/turboIndex";
    }

    @GetMapping("/eng")
    public String indexEngPage() {
        return "/eng/Index.html";
    }


    /*@GetMapping("/contact")
    public String contactPage(Model model,
                              @RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postList = postService.getPostList(pageable);
        model.addAttribute("posts", postList);
        return  "th/Turbo/turboContack.html";
    }*/

    @GetMapping("/post/edit/{id}")
    public String editPost(@PathVariable long id, Model model)
    {
        Post post = postRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("post", postService.getPost(id));

        return "th/Turbo/turboContactUpdate";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable long id, Model model)
    {
        Post post = postRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("post", postService.getPost(id));

        return "th/Turbo/turboContactDetail";
    }

    @GetMapping("/post/insert")
    public String insertPostPage() {
        return "th/Turbo/turboContactInsert";
    }

    // 여기서부터 정적 웹페이지니까 이 컨트롤러들 다 지우고 html 페이지를 가져오는 방식으로.
    /*@GetMapping("/company")
    public String companyPage() {
        return "th/Turbo/company/company";
    }

    @GetMapping("/company/ceo")
    public String companyCeoPage() {
        return "th/Turbo/company/ceo";
    }

    @GetMapping("/coretech")
    public String coretechPage() {
        return "th/Turbo/coretech/coretech";
    }

    @GetMapping("product")
    public String productPage() {
        return "th/Turbo/product/product";
    }*/

    // 다시 mvc로 돌리기 위한 주석
    /*@GetMapping("/admin/usermanage/{id}")
    public String getUser() { return "th/adminUpdateUser"; }

    @GetMapping("/admin")
    public String adminPage() { return "th/adminPage"; }

    @GetMapping("/user/login")
    public String loginPage() {
        return "th/login";
    }

    @GetMapping("/user/signUp")
    public String signUpPage() {
        return "th/signup";
    }

    @GetMapping("/post/{id}")
    public String getPostPage() {
        return "th/getPost";
    }

    @GetMapping("/")
    public String getIndexPage() { return "th/index"; }

    @GetMapping("/post/insert")
    public String insertPostPage() {
        return "th/savePost";
    }*/
}
