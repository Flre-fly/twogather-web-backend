package com.twogather.twogatherwebbackend.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twogather.twogatherwebbackend.domain.*;
import com.twogather.twogatherwebbackend.dto.category.CategoryResponse;
import com.twogather.twogatherwebbackend.repository.CategoryRepository;
import com.twogather.twogatherwebbackend.repository.ReviewRepository;
import com.twogather.twogatherwebbackend.repository.StoreOwnerRepository;
import com.twogather.twogatherwebbackend.repository.store.StoreRepository;
import com.twogather.twogatherwebbackend.service.CategoryService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.twogather.twogatherwebbackend.acceptance.TestHelper.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StoreOwnerRepository ownerRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EntityManager em;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    @Transactional
    public void whenGetAllCategories_ThenAllCategoryList() throws Exception {
        Category category1 = new Category("category1");
        Category category2 = new Category("category2");
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryRepository.saveAll(categoryList);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].categoryId").exists())
                .andExpect(jsonPath("$.data[0].name").exists())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @Transactional
    public void whenSetCategoriesForStore_ThenAssociateCategoryWithStore() throws Exception {
        //given
        StoreOwner owner = createOwner(ownerRepository, passwordEncoder);
        Store store = createStore(storeRepository,owner);

        Category category = categoryRepository.save(new Category("categoryName"));
        Long storeId = store.getStoreId();
        Long categoryId = category.getCategoryId();

        createAuthority(owner);

        CategoryResponse response = categoryService.setCategoriesForStore(storeId, categoryId);
        String url = "/api/stores/" + storeId + "/categories/" + categoryId;
        mockMvc.perform(patch(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.categoryId").value(response.getCategoryId()))
                .andExpect(jsonPath("$.data.name").value(response.getName()))
                .andDo(MockMvcResultHandlers.print());
    }
}