package sample.my.cv.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaQuery;
import sample.my.cv.model.User;

@Stateless
public class UserDao extends GenericDAO<User> {

    public UserDao() {
        super(User.class);
    }

    // Using the unchecked because JPA does not have a
    // em.getCriteriaBuilder().createQuery()<T> method
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<User> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

}
