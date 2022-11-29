package com.api.dmat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.api.dmat.model.QuestionsMaster;

public interface QuestionMasterRepo extends JpaRepository<QuestionsMaster, Integer> {

	QuestionsMaster findByQuestionid(int questionid);

	boolean existsByQuestionid(int questionId);

	@Transactional
	@Modifying
	@Query(value = "select * from questionmaster", nativeQuery = true)
	public Iterable<QuestionsMaster> findProcessQuestions();

	// Gets the max score that is possible for a DisplayCategory
	@Query(value = "select sum(maxscore) from displaycategoriesmaster d inner join questionsmaster q on d.displaycategoryid=q.displaycategoryid where q.displaycategoryid=:id ", nativeQuery = true)
	String maxScoreByDisplayCategoryId(@Param("id") Integer id);

	// Gets the max score that is possible for a DisplayCategory
	@Query(value = "select sum(maxscore) from displaycategoriesmaster d inner join questionsmaster q on d.displaycategoryid=q.displaycategoryid where q.devopscategoryid=:id ", nativeQuery = true)
	String maxScoreByDevopsPracticesId(@Param("id") Integer id);
}
