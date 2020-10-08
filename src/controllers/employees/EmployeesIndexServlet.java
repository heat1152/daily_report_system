package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EmployeesIndexServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        }catch(NumberFormatException e){}

//        従業員情報
        List<Employee> employees = em.createNamedQuery("getAllEmployees",Employee.class)
                .setFirstResult(15*(page - 1))
                .setMaxResults(15)
                .getResultList();
//      フォロー状況
        List<Employee> follows = em.createNamedQuery("getAllFollow",Employee.class)
                .setParameter("login_employee", login_employee)
                .setFirstResult(15*(page - 1))
                .setMaxResults(15)
                .getResultList();
//      フォロワー状況
        List<Employee> followers = em.createNamedQuery("getAllFollower",Employee.class)
                .setParameter("login_employee", login_employee)
                .setFirstResult(15*(page - 1))
                .setMaxResults(15)
                .getResultList();




        long employees_count = em.createNamedQuery("getEmployeesCount", Long.class)
                .getSingleResult();

        em.close();

        request.setAttribute("followers", followers);
        request.setAttribute("follows", follows);
        request.setAttribute("login_employee", login_employee);
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);

        if(request.getSession().getAttribute("flush") != null){
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);
    }

}
