function getQuizIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('quizId');
  }

  function createQuestionRow(question = {}) {
    const q = question;
    return `
      <tr data-id="${q.questionId || ''}">
        <td><input type="text" class="form-control questionDescrioption" placeholder="Enter question" value="${q.questionDescrioption || ''}" /></td>
        <td><input type="text" class="form-control option1" placeholder="Option 1" value="${q.option1 || ''}" /></td>
        <td><input type="text" class="form-control option2" placeholder="Option 2" value="${q.option2 || ''}" /></td>
        <td><input type="text" class="form-control option3" placeholder="Option 3" value="${q.option3 || ''}" /></td>
        <td><input type="text" class="form-control option4" placeholder="Option 4" value="${q.option4 || ''}" /></td>
        <td><input type="number" min="1" max="4" class="form-control answerOption" placeholder="Correct Option (1-4)" value="${q.answerOption || ''}" /></td>
        <td><button class="btn btn-danger btn-sm btn-remove">Delete</button></td>
      </tr>
    `;
  }

  function loadQuestions(quizId) {
    $.ajax({
      url: `http://localhost:2025/api/questions`,
      method: 'GET',
      success: function(response) {
        if (response && response.details) {
          const questions = response.details.filter(q => q.quiz && q.quiz.quizId == quizId);
          $('#questionsTable tbody').empty();
          questions.forEach(q => {
            $('#questionsTable tbody').append(createQuestionRow(q));
          });
        }
      },
      error: function() {
        alert('Failed to load questions.');
      }
    });
  }

  $('#addQuestionBtn').on('click', function() {
    $('#questionsTable tbody').append(createQuestionRow());
  });

  $('#questionsTable').on('click', '.btn-remove', function() {
    const row = $(this).closest('tr');
    const questionId = row.data('id');
    if (questionId) {
      $.ajax({
        url: `http://localhost:2025/api/questions/${questionId}`,
        method: 'DELETE',
        success: function() {
          row.remove();
        },
        error: function() {
          alert('Failed to delete question.');
        }
      });
    } else {
      row.remove();
    }
  });

  $('#submitQuestionsBtn').on('click', function() {
    const quizId = getQuizIdFromUrl();
    if (!quizId) {
      alert('Quiz ID not found in URL.');
      return;
    }
    const rows = $('#questionsTable tbody tr');
    if (rows.length === 0) {
      alert('Please add at least one question.');
      return;
    }
    let successCount = 0;
    let failCount = 0;
    rows.each(function() {
      const row = $(this);
      const questionId = row.data('id');
      const questionData = {
        questionId: questionId || null,
        questionDescrioption: row.find('.questionDescrioption').val(),
        option1: row.find('.option1').val(),
        option2: row.find('.option2').val(),
        option3: row.find('.option3').val(),
        option4: row.find('.option4').val(),
        answerOption: parseInt(row.find('.answerOption').val()),
        quiz: { quizId: parseInt(quizId) }
      };
      // Validate required fields
      if (!questionData.questionDescrioption || !questionData.option1 || !questionData.option2 || !questionData.option3 || !questionData.option4 || !questionData.answerOption) {
        alert('Please fill in all required fields for each question.');
        failCount++;
        return true; 
      }
      if (questionData.answerOption < 1 || questionData.answerOption > 4) {
        alert('Correct option must be between 1 and 4.');
        failCount++;
        return true; 
      }
      $.ajax({
        url: 'http://localhost:2025/api/questions',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(questionData),
        success: function(response) {
          successCount++;
          if (response && response.details && response.details.questionId) {
            row.attr('data-id', response.details.questionId);
          }
          if (successCount + failCount === rows.length) {
            alert(`Questions saved. Success: ${successCount}, Failed: ${failCount}`);
            window.location.href = '../quiz/quiz.html';
          }
        },
        error: function() {
          failCount++;
          if (successCount + failCount === rows.length) {
            alert(`Questions saved. Success: ${successCount}, Failed: ${failCount}`);
            window.location.href = '../quiz/quiz.html';
          }
        }
      });
    });
  });

  $(document).ready(function() {
    const quizId = getQuizIdFromUrl();
    if (!quizId) {
      alert('Quiz ID is missing in URL.');
      window.location.href = '../quiz/quiz.html';
      return;
    }
    loadQuestions(quizId);
  });