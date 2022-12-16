/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookaroomrestfulclient.beans;

import bookaroomrestfulclient.client.PersistenceClient;
import bookaroomrestfulclient.models.Comments;
import bookaroomrestfulclient.models.Users;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

/**
 *
 * @author jingminwang
 */
@Named(value = "commentBean")
@SessionScoped
public class CommentBean implements Serializable {
    
    
    private String comment = "";
    private LocalDateTime now = LocalDateTime.now();
    private DateTimeFormatter formatterComment = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Integer rating;
    
    public List<Comments> getComments() {
        return PersistenceClient.getInstance().getAllComments();
    }
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public LocalDateTime getTodayDate() {
        return now;
    }
    
    @Transactional
    public void addCommentFromUser() {
     
        Comments newComment = new Comments();
        Users user = LoginBean.getUserLoggedIn();
        comment = user.getUsername()+": "+comment+" ("+getTodayDate().format(formatterComment)+")";
        newComment.setComment(comment);
        newComment.setRating(rating);
        //empty values
        this.comment = "";
        this.rating = 0;

    }   
    
    public Integer getRating(){
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
}