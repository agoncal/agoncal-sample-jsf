package org.agoncal.sample.jsf.video;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Named
@Stateful
@ConversationScoped
public class TalkBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Long id;
    private Talk talk;
    private List<Talk> pageItems;
    private Talk example = new Talk();
    private Part uploadedVideo;

    @Inject
    private Conversation conversation;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    // ======================================
    // =          Business Methods          =
    // ======================================

    public String uploadVideo() throws IOException {
        InputStream is = uploadedVideo.getInputStream();
        Files.copy(is, Paths.get("/Users/antoniombp/Documents/Code/github/agoncal-sample-jsf/01-Video/target/sampleJSFVideo/videos/" + talk.getId() + ".mp4"));
        return null;
    }

    public String create() {

        this.conversation.begin();
        return "create?faces-redirect=true";
    }

    public void retrieve() {

        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        if (this.conversation.isTransient()) {
            this.conversation.begin();
        }

        if (this.id == null) {
            this.talk = this.example;
        } else {
            this.talk = findById(getId());
        }
    }

    public Talk findById(@NotNull Long id) {

        return this.entityManager.find(Talk.class, id);
    }

    public String update() {
        this.conversation.end();

        try {
            if (this.id == null) {
                this.entityManager.persist(this.talk);
                return "search?faces-redirect=true";
            } else {
                this.entityManager.merge(this.talk);
                return "view?faces-redirect=true&id=" + this.talk.getId();
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public String delete() {
        this.conversation.end();

        try {
            Talk deletableEntity = findById(getId());

            this.entityManager.remove(deletableEntity);
            this.entityManager.flush();
            return "search?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            return null;
        }
    }

    public void paginate() {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

        CriteriaQuery<Talk> criteria = builder.createQuery(Talk.class);
        Root<Talk> root = criteria.from(Talk.class);
        TypedQuery<Talk> query = this.entityManager.createQuery(criteria.select(root).where(getSearchPredicates(root)));
        this.pageItems = query.getResultList();
    }

    private Predicate[] getSearchPredicates(Root<Talk> root) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String title = this.example.getTitle();
        if (title != null && !"".equals(title)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("title")), "%" + title.toLowerCase() + "%"));
        }
        String room = this.example.getRoom();
        if (room != null && !"".equals(room)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("room")), '%' + room.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Talk getTalk() {
        return this.talk;
    }

    public Talk getExample() {
        return this.example;
    }

    public void setExample(Talk example) {
        this.example = example;
    }

    public List<Talk> getPageItems() {
        return this.pageItems;
    }

    public Part getUploadedVideo() {
        return uploadedVideo;
    }

    public void setUploadedVideo(Part uploadedVideo) {
        this.uploadedVideo = uploadedVideo;
    }
}