package jobs;

import models.person.Person;
import org.hibernate.Session;
import play.db.jpa.JPA;
import play.jobs.OnApplicationStart;

import javax.persistence.EntityManager;

@OnApplicationStart
public class StartUp extends BaseJob {
    
    @Override
    public void doJob() throws Exception {
        initAdmin();
        updateColumn();
    }
    
    private static void initAdmin() {
        final Session s = (Session) JPA.em().getDelegate();
        if (!s.getTransaction().isActive()) {
            s.getTransaction().begin();
        }
        Person.initAdmin();
        Person.initPinYin();
        s.getTransaction().commit();
    }
    
    private static void updateColumn() {
        EntityManager em = JPA.em();
        Session s = (Session) em.getDelegate();
        if (!s.getTransaction().isActive())
            s.getTransaction().begin();
        s.getTransaction().commit();
    }
}
