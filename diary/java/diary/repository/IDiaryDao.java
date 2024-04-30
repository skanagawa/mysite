package diary.repository;

import java.util.List;

import diary.entity.Diary;
import diary.form.GetForm;
import diary.form.PostForm;
import diary.form.PutForm;
 
public interface IDiaryDao {
    // 登録されている日記を取得
    List<Diary> findList(GetForm form);
    
 // 日記を登録する
    int insert(PostForm form);// 日記を登録する
    
 // idを指定して日記を1件取得
    Diary findById(int id);
      
 // 日記を更新する
    int update(PutForm form);   
    
    
 // 日記を削除する
    int delete(int id);
    
}
