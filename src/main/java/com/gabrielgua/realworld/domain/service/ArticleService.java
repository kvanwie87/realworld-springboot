package com.gabrielgua.realworld.domain.service;

import com.gabrielgua.realworld.domain.exception.ArticleAlreadyRegisteredException;
import com.gabrielgua.realworld.domain.exception.ArticleNotFoundException;
import com.gabrielgua.realworld.domain.model.Article;
import com.gabrielgua.realworld.domain.model.Profile;
import com.gabrielgua.realworld.domain.model.Tag;
import com.gabrielgua.realworld.domain.model.User;
import com.gabrielgua.realworld.domain.repository.ArticleRepository;
import com.gabrielgua.realworld.infra.spec.ArticleSpecification;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final Slugify slg;
    private final ArticleRepository repository;

    @Transactional(readOnly = true)
    public Page<Article> listAll(ArticleSpecification filter, Pageable pageable) {
        return repository.findAll(filter, pageable);
    }

    @Transactional(readOnly = true)
    public Article getBySlug(String slug) {
        return repository.findBySlug(slug).orElseThrow(() -> new ArticleNotFoundException(slug));
    }

    @Transactional(readOnly = true)
    public List<Article> getFeedByUser(User user, Pageable pageable) {
        List<User> followedUsers = user.getFollowing().stream().map(Profile::getUser).toList();

        return repository.findAllByAuthorIn(followedUsers, pageable);
    }

    @Transactional
    public Article save(Article article, Profile profile, List<Tag> tags) {
        var slug = slg.slugify(article.getTitle());

        checkSlugAvailability(slug, article);

        if (article.getId() == null) {
            addAllTags(article, tags);
            article.setAuthor(profile);
        }

        article.setSlug(slug);
        return repository.save(article);
    }

//    @Transactional
//    public Article update(Article article) {
//        var slug = slg.slugify(article.getTitle());
//
//        article.setSlug(slug);
//        checkSlugAvailability(article);
//    }



    private boolean slugTaken(String slug, Article article) {
        var existingArticle = repository.findBySlug(slug);
        return existingArticle.isPresent() && !existingArticle.get().equals(article);
    }

    @Transactional
    public Article userFavorited(User user, Article article) {
        article.addFavorite(user);
        return repository.save(article);
    }

    @Transactional
    public Article userUnfavorited(User user, Article article) {
        article.removeFavorite(user);
        return repository.save(article);
    }

    private void checkSlugAvailability(String slug, Article article) {
        if (slugTaken(slug, article)) throw new ArticleAlreadyRegisteredException(slug);
    }

    private void addAllTags(Article article, List<Tag> tags) {
        article.setTagList(new HashSet<>());
        tags.forEach(article::addTag);
    }
}