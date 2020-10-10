package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

@WebServlet("/reports/update")
public class ReportsUpdateSerlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReportsUpdateSerlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())){
            EntityManager em = DBUtil.createEntityManager();

            Report r = em.find(Report.class, request.getSession().getAttribute("report_id"));

            r.setReport_date(Date.valueOf(request.getParameter("report_date")));
            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));
            r.setUpdated_at(new Timestamp(System.currentTimeMillis()));

            try {
                String Astr = request.getParameter("report_Attendance_time");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    java.util.Date date = sdf.parse(Astr);
                    sdf.applyPattern("yyyy-MM-dd HH:mm");
                    Timestamp timestamp = new Timestamp(date.getTime());

                    r.setAttendance_time(timestamp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    String Lstr = request.getParameter("report_Leave_time");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    java.util.Date date = sdf.parse(Lstr);
                    sdf.applyPattern("yyyy-MM-dd HH:mm");
                    Timestamp timestamp = new Timestamp(date.getTime());

                    r.setLeave_time(timestamp);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

            List<String> errors = ReportValidator.validate(r);
            if(errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("report", r);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "更新が完了しました。");

                request.getSession().removeAttribute("report_id");

                response.sendRedirect(request.getContextPath() + "/reports/index");
            }
        }
    }

}
