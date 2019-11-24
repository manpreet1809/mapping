package com.mhp.coding.challenges.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhp.coding.challenges.mapping.exception.ArticleIdNotFound;
import com.mhp.coding.challenges.mapping.models.dto.ArticleDto;
import com.mhp.coding.challenges.mapping.models.dto.blocks.ArticleBlockDto;
import com.mhp.coding.challenges.mapping.services.ArticleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleService articleService;

    @Test
    public void getArticleBlocks_shouldBeSorted() throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(getRootUrl() + "/article", String.class);
        String responseString = response.getBody();
        Assert.assertNotNull(responseString);
        List<ArticleDto> articles = Arrays.asList(toObject(responseString, ArticleDto[].class));
        articles.forEach(articleDto -> {
            List<Integer> sortIndexBlock = articleDto.getBlocks().stream()
                    .map(ArticleBlockDto::getSortIndex)
                    .collect(Collectors.toList());

            Assert.assertEquals(articleDto.getBlocks().stream()
                    .map(ArticleBlockDto::getSortIndex)
                    .sorted(Integer::compareTo)
                    .collect(Collectors.toList()), sortIndexBlock);
        });
    }

    @Test(expected = ArticleIdNotFound.class)
    public void getDetails_whenArticleNotFound() {
        given(articleService.articleForId(anyLong())).willThrow(new ArticleIdNotFound("Article not found"));
        ResponseEntity<String> response = restTemplate.getForEntity(getRootUrl() + "/article/99", String.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getDetails_whenArticleFound() {
        Long id = 1001L;
        final ArticleDto article = articleService.articleForId(id);
        Assert.assertEquals("Id doesn't match", id, article.getId());
    }

    @Test
    public void getDetails_JsonFormatted() throws JsonProcessingException {
        Long id = 1001L;
        final ArticleDto article = articleService.articleForId(id);
        String articleJson = toJson(article);
        ResponseEntity<String> response = restTemplate.getForEntity(getRootUrl() + "/article/" + id, String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assert.assertEquals("Json doesn't match", articleJson, response.getBody());

    }

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    private String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    private <T> T toObject(String json, Class<T> type) throws IOException {
        return objectMapper.readValue(json, type);

    }
}