package controllers.follow;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

@WebServlet("/follow/update")
public class FollowUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public FollowUpdateServlet() {
        super();

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Employee e =new Employee();
        Follow f = new Follow();


        int follow_update_check = Integer.parseInt(request.getParameter("follow_update_check"));

        //フォロー解除処理をするか、フォロー処理をするかのチェック follow_update_checkが0の場合「フォロー解除」1の場合「フォロー」処理をする。
        if(follow_update_check == 1 && follow_update_check != 0){
            //フォロー解除処理
            e = em.find(Employee.class, Integer.parseInt(request.getParameter("follow_employee_id")));

            f = em.createNamedQuery("getFollowSearch",Follow.class)
                    .setParameter("login_employee", login_employee)
                    .setParameter("follow_employee", e)
                            .getSingleResult();

            em.getTransaction().begin();
            em.remove(f);       // データ削除
            em.getTransaction().commit();
            em.close();
        }else{
            e = em.find(Employee.class, Integer.parseInt(request.getParameter("follow_employee_id")));
            f.setUser_id(login_employee);
            f.setFollow_id(e);

            em.getTransaction().begin();
            em.persist(f);
            em.getTransaction().commit();
            em.close();
            }
        response.sendRedirect(request.getContextPath() + "/employees/index");
        }

}
