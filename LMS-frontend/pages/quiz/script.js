function fetchAndRenderQuestions(quizId) {
    $.ajax({
      url: `http://localhost:2025/api/quizes/quiz/${quizId}`,
      type: "GET",
      dataType: "json",
      success: function (response) {
        if (!response.details || response.details.length === 0) {
          $("#questions-container").html("<p>No questions found.</p>");
          return;
        }
        const quizInfo = response.details[0].quiz;
        const minPassScore = quizInfo.minPassScore;
        const totalMarks = quizInfo.marks;

        const questions = response.details.map((q) => ({
          id: q.questionId,
          question: q.questionDescrioption,
          options: [q.option1, q.option2, q.option3, q.option4],
          correct: q.options
            ? q.options[q.answerOption - 1]
            : q[`option${q.answerOption}`],
        }));

        renderQuestions(questions, minPassScore, totalMarks);
      },
      error: function (xhr, status, error) {
        window.location.href = "../error/index.html";
      },
    });
  }

  function renderQuestions(questions, minPassScore, totalMarks) {
    questionsContainer.innerHTML = "";
    selectedAnswers = {};
    questions.forEach((q) => {
      const card = document.createElement("div");
      card.className = "question-card";

      const title = document.createElement("div");
      title.className = "question-title";
      title.textContent = q.question;
      card.appendChild(title);

      const optionsList = document.createElement("ul");
      optionsList.className = "options";

      q.options.forEach((opt, idx) => {
        const li = document.createElement("li");
        const optionId = `q${q.id}-opt${idx}`;
        const radio = document.createElement("input");
        radio.type = "radio";
        radio.name = `question-${q.id}`;
        radio.id = optionId;
        radio.value = opt;
        radio.addEventListener("change", () => {
          selectedAnswers[q.id] = opt;
        });

        const label = document.createElement("label");
        label.htmlFor = optionId;
        label.textContent = opt;

        li.appendChild(radio);
        li.appendChild(label);
        optionsList.appendChild(li);
      });

      card.appendChild(optionsList);
      questionsContainer.appendChild(card);
    });

    submitBtn.onclick = function () {
      let score = 0;
      questions.forEach((q) => {
        if (selectedAnswers[q.id] === q.correct) {
          score++;
        }
      });
      const marksPerQuestion = totalMarks / questions.length;
      const obtainedMarks = score * marksPerQuestion;
      const passOrFail = obtainedMarks >= minPassScore ? "Pass" : "Fail";
      scoreDisplay.textContent = `Your score: ${score} / ${questions.length} (${obtainedMarks} marks) - ${passOrFail}`;
    };
  }

  const quizIdRaw = localStorage.getItem("quizIds");
  const quizId = quizIdRaw ? quizIdRaw.replace(/^"|"$/g, "") : "";
  const questionsContainer = document.getElementById("questions-container");
  const submitBtn = document.getElementById("submit-btn");
  const scoreDisplay = document.getElementById("score-display");

  fetchAndRenderQuestions(quizId);