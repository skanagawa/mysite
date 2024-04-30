package diary.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import diary.entity.Diary;
import diary.form.GetForm;
import diary.form.PostForm;
import diary.form.PutForm;
 
@Repository
public class DiaryDao implements IDiaryDao {
     
    private final NamedParameterJdbcTemplate jdbcTemplate;
 
    @Autowired
    public DiaryDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
 // 日記を登録する
    @Override
    public int insert(PostForm form) {
        // 登録件数を格納
        int count = 0;
        String sql = "INSERT INTO diary(category, title, content, date , update_datetime) "
                + "VALUES(:category, :title, :content, :date , :update_datetime)";
        // パラメータ設定用Map
        Map<String, Object> param = new HashMap<>();
        param.put("category", form.getCategoryForm());
        param.put("title", form.getTitleForm());
        param.put("content", form.getContentForm());
        param.put("date", form.getDateForm());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        param.put("update_datetime", timestamp);
        count = jdbcTemplate.update(sql, param);
        return count;
    }
 
    @Override
    public List<Diary> findList(GetForm form) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT d.id, d.category, d.title, d.content, TO_CHAR(d.date, 'YYYY/MM/DD') AS date, d.update_datetime, c.name "
                + "FROM public.diary AS d INNER JOIN category_code AS c ON d.category = c.cd "
                + "WHERE c.group_cd = '01'");
 
        // パラメータ設定用Map
        Map<String, String> param = new HashMap<>();
     // パラメータが存在した場合、where句にセット
        if(form.getCategory() != null && form.getCategory() != "") {
            sqlBuilder.append(" AND c.cd = :cd");
            param.put("cd", form.getCategory());
        }
        if(form.getDate() != null && form.getDate() != "") {
            sqlBuilder.append(" AND TO_CHAR(d.date, 'YYYY/MM') = :date");
            param.put("date", form.getDate());
        }
     
        String sql = sqlBuilder.toString();
     
        //タスク一覧をMapのListで取得
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, param);
        //return用の空のListを用意
        List<Diary> list = new ArrayList<Diary>();
 
        //データをDiaryにまとめる
        for(Map<String, Object> result : resultList) {
            Diary diary = new Diary();
            diary.setId((int)result.get("id"));
            diary.setCategory((String)result.get("category"));
            diary.setTitle((String)result.get("title"));
            diary.setContent((String)result.get("content"));
            diary.setDate((String)result.get("date"));
            diary.setUpdate_datetime((Timestamp)result.get("update_datetime"));
            diary.setName((String)result.get("name"));
            list.add(diary);
        }
        return list;
    }
    
    @Override
    public Diary findById(int id) throws  IncorrectResultSizeDataAccessException {
        String sql = "SELECT d.id, d.category, d.title, d.content, TO_CHAR(d.date, 'YYYY/MM/DD') AS date, d.update_datetime, c.name "
                + "FROM diary AS d INNER JOIN category_code AS c ON d.category = c.cd "
                + "WHERE d.id = :id";
     
        // パラメータ設定用Map
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        // 一件取得
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, param);
        Diary diary = new Diary();
        diary.setId((int)result.get("id"));
        diary.setCategory((String)result.get("category"));
        diary.setTitle((String)result.get("title"));
        diary.setContent((String)result.get("content"));
        diary.setDate((String)result.get("date"));
        diary.setUpdate_datetime((Timestamp)result.get("update_datetime"));
        diary.setName((String)result.get("name"));
         
        return diary;
    }
    
 // 日記を編集する
    @Override
    public int update(PutForm form) {
        int count = 0;
        String sql = "UPDATE diary "
                + "SET category=:category, title=:title, content=:content, date=:date, update_datetime=:update_datetime "
                + "WHERE id=:id";
        // パラメータ設定用Map
        Map<String, Object> param = new HashMap<>();
        param.put("id", form.getId());
        param.put("category", form.getCategoryForm());
        param.put("title", form.getTitleForm());
        param.put("content", form.getContentForm());
        param.put("date", form.getDateForm());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        param.put("update_datetime", timestamp);
        count = jdbcTemplate.update(sql, param);
        return count;
    }
    
 // 日記を削除する
    @Override
    public int delete(int id) {
        int count = 0;
        String sql = "DELETE FROM diary "
                + "WHERE id = :id";
        // パラメータ設定用Map
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        count = jdbcTemplate.update(sql, param);
        return count;
    }
    
    
    
    
    
    
    
}