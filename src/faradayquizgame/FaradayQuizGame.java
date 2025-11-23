/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package faradayquizgame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

/**
 * Jogo de perguntas e respostas sobre o artigo
 * "Faraday effects emerging from the optical magnetic field".
 *
 * - Aumentado para 15 perguntas.
 * - Pede o nome do jogador.
 * - Salva pontuação em arquivo CSV: faraday_quiz_scores.csv
 */
public class FaradayQuizGame extends JFrame {

    // Componentes da interface
    private JTextArea questionArea;
    private JButton[] optionButtons;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JButton nextButton;
    private JButton restartButton;
    private JButton exitButton;

    // Lógica do jogo
    private Question[] questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;

    // Nome do jogador e arquivo de pontuações
    private String playerName;
    private static final String SCORE_FILE = "faraday_quiz_scores.csv";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FaradayQuizGame game = new FaradayQuizGame();
            game.setVisible(true);
        });
    }

    public FaradayQuizGame() {
        setTitle("Quiz - Efeito Faraday & Campo Magnético Óptico");
        setSize(880, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Pergunta o nome do jogador
        askPlayerName();

        createQuestions();
        createUI();
        loadQuestion(0);
    }

    // Pergunta o nome do aluno/colega
    private void askPlayerName() {
        playerName = JOptionPane.showInputDialog(
                this,
                "Digite seu nome (ou apelido) para registrar a pontuação:",
                "Identificação do jogador",
                JOptionPane.QUESTION_MESSAGE
        );
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Anonimo";
        }
    }

    // Cria o banco de perguntas sobre o artigo
    private void createQuestions() {
        questions = new Question[]{

            new Question(
                "1) Qual é a ideia principal do artigo em relação ao campo magnético óptico?",
                new String[]{
                    "Que o campo magnético da luz é desprezível em magneto-ótica.",
                    "Que apenas o campo elétrico óptico contribui para o efeito Faraday.",
                    "Que o campo magnético óptico também contribui para o efeito Faraday e o inverso Faraday.",
                    "Que o efeito Faraday não existe em materiais paramagnéticos."
                },
                2,
                "O artigo mostra que o componente magnético da luz não é negligenciável: "
                + "ele gera torque via equação de Landau-Lifshitz-Gilbert e contribui tanto "
                + "para o inverso efeito Faraday (IFE) quanto para o Faraday direto (FE)."
            ),

            new Question(
                "2) O que é o efeito Faraday (FE) clássico descrito no artigo?",
                new String[]{
                    "Rotação da polarização devido a um campo elétrico estático.",
                    "Rotação da polarização causada por um campo magnético estático que induz birrefringência circular.",
                    "Emissão de fótons por um ímã aquecido.",
                    "Apenas o aumento da intensidade da luz em um meio magnético."
                },
                1,
                "O FE é a rotação do plano de polarização de um feixe óptico quando ele "
                + "propaga em um material sob campo magnético estático, devido à birrefringência circular induzida."
            ),

            new Question(
                "3) O que é o inverso efeito Faraday (IFE) na visão tradicional de Pershan?",
                new String[]{
                    "Uma rotação de polarização gerada por campo magnético estático.",
                    "Uma magnetização induzida pelo campo elétrico óptico em um processo não linear.",
                    "Um efeito puramente térmico sem dependência de polarização.",
                    "Um fenômeno que ocorre apenas com luz linearmente polarizada."
                },
                1,
                "Na formulação de Pershan, o IFE é uma magnetização induzida pelo campo elétrico óptico "
                + "por meio de uma suscetibilidade magneto-óptica não linear de segunda ordem."
            ),

            new Question(
                "4) Qual equação de dinâmica de spins o artigo usa para descrever o torque do "
              + "campo magnético óptico?",
                new String[]{
                    "Equação de Schrödinger independente do tempo.",
                    "Equação de Maxwell em forma integral.",
                    "Equação de Landau-Lifshitz-Gilbert (LLG).",
                    "Equação de Navier-Stokes."
                },
                2,
                "O artigo usa a equação de Landau-Lifshitz-Gilbert (LLG), que descreve a precessão "
                + "e o amortecimento (damping) do vetor magnetização sob campos magnéticos efetivos."
            ),

            new Question(
                "5) A razão adimensional η = αγ H_opt / f_opt, introduzida no artigo, "
              + "relaciona-se a que grandeza física?",
                new String[]{
                    "À razão entre campo elétrico e campo magnético.",
                    "À força de interação entre o campo magnético óptico e a magnetização.",
                    "À temperatura do material.",
                    "Ao índice de refração do meio."
                },
                1,
                "η quantifica a força do torque induzido pelo campo magnético óptico na LLG, "
                + "dependendo do damping de Gilbert α, da amplitude H_opt e da frequência óptica f_opt."
            ),

            new Question(
                "6) No artigo, o torque longitudinal Tz gerado por um pulso circularmente polarizado "
              + "apresenta qual dependência com a fluência óptica F?",
                new String[]{
                    "Tz é independente de F.",
                    "Tz é proporcional à raiz quadrada de F.",
                    "Tz é inversamente proporcional a F.",
                    "Tz é linear em F (Tz ∝ F)."
                },
                3,
                "As simulações mostram que Tz cresce linearmente com a fluência F, pois Tz ∝ H_peak^2 · τ_p, "
                + "e a fluência é proporcional a H_peak^2 · τ_p."
            ),

            new Question(
                "7) Para um feixe com polarização geral, o artigo mostra que o torque longitudinal Tz "
              + "é proporcional a qual combinação de intensidades?",
                new String[]{
                    "À soma I_RCP + I_LCP.",
                    "À diferença I_RCP − I_LCP.",
                    "Apenas à intensidade da componente linear horizontal.",
                    "Apenas à intensidade da componente linear vertical."
                },
                1,
                "O torque longitudinal é máximo para luz circularmente polarizada e nulo para luz linear; "
                + "matematicamente Tz ∝ I_RCP − I_LCP."
            ),

            new Question(
                "8) O que o artigo conclui sobre a contribuição do campo magnético óptico para o "
              + "Verdet constant do cristal TGG em 800 nm?",
                new String[]{
                    "É desprezível, menor que 1% do valor medido.",
                    "Responde por aproximadamente 5% do valor medido.",
                    "Responde por cerca de 17–18% do valor medido.",
                    "Explica totalmente (100%) o valor medido."
                },
                2,
                "Com os parâmetros do TGG, o modelo via LLG fornece um Verdet que corresponde a "
                + "≈17,5% do valor experimental em 800 nm; o restante vem de mecanismos ligados ao campo elétrico óptico."
            ),

            new Question(
                "9) Segundo o artigo, o que acontece com a relação de reciprocidade entre o "
              + "efeito Faraday (FE) e o inverso Faraday (IFE) em escalas de tempo ultrarrápidas?",
                new String[]{
                    "A reciprocidade se mantém rigorosamente.",
                    "A reciprocidade se quebra: FE e IFE não podem ser descritos pelo mesmo Verdet.",
                    "O FE deixa de existir nessas escalas.",
                    "O IFE torna-se puramente térmico."
                },
                1,
                "Os autores mostram que, no regime fora de equilíbrio (pulso ultrarrápido), "
                + "os Verdet constants derivados para FE e IFE a partir da LLG são diferentes, "
                + "o que reflete a quebra de reciprocidade observada em experimentos."
            ),

            new Question(
                "10) Por que a contribuição do campo magnético óptico para o FE torna-se relativamente "
              + "mais importante em comprimentos de onda longos (≈1,3 µm)?",
                new String[]{
                    "Porque o índice de refração tende a zero.",
                    "Porque o Verdet associado ao campo elétrico diminui com o aumento de λ, "
                    + "enquanto a parte de origem magnética é aproximadamente independente de λ.",
                    "Porque o campo magnético óptico some em altas frequências.",
                    "Porque o TGG deixa de ser transparente nessa região espectral."
                },
                1,
                "Os dados experimentais mostram que o |V| total cai com λ, enquanto o termo calculado "
                + "via LLG ligado ao campo magnético é quase constante; assim, a fração relativa dessa "
                + "contribuição cresce para comprimentos de onda maiores."
            ),

            new Question(
                "11) Em que tipo de material o artigo implementa numericamente o modelo do "
              + "campo magnético óptico?",
                new String[]{
                    "Em um metal ferromagnético opaco.",
                    "Em um isolante diamagnético transparente.",
                    "Em um cristal paramagnético transparente de TGG (gadolínio-gálio granada).",
                    "Em um semicondutor dopado com elétrons livres."
                },
                2,
                "Os autores usam como caso de estudo o TGG (terbium gallium garnet), "
                + "um cristal paramagnético transparente muito usado em dispositivos Faraday."
            ),

            new Question(
                "12) Na LLG usada no artigo, o termo de amortecimento (damping) com constante α "
              + "tem qual efeito principal sobre a magnetização?",
                new String[]{
                    "Faz a magnetização crescer indefinidamente.",
                    "Causa precessão sem mudança de módulo.",
                    "Relaxar a magnetização em direção ao campo efetivo, dissipando energia.",
                    "Inverter instantaneamente o sinal da magnetização."
                },
                2,
                "O termo de amortecimento de Gilbert com parâmetro α faz com que a magnetização "
                + "relaxe em direção ao campo efetivo, representando a dissipação de energia do sistema de spins."
            ),

            new Question(
                "13) O artigo discute que o campo magnético óptico pode ser especialmente relevante "
              + "em qual regime temporal?",
                new String[]{
                    "Regime estacionário de CW (onda contínua) e tempos longos.",
                    "Regime de pulsos ultracurtos, na escala de femtossegundos.",
                    "Regime DC (campos constantes).",
                    "Regime puramente térmico, sem oscilação óptica."
                },
                1,
                "A contribuição magnética é analisada para pulsos ultracurtos (fs), onde a resposta "
                + "transiente da magnetização é importante e a reciprocidade FE/IFE pode se quebrar."
            ),

            new Question(
                "14) Em relação à polarização da luz incidente, em qual caso o torque longitudinal "
              + "Tz calculado é máximo?",
                new String[]{
                    "Para luz linearmente polarizada horizontal.",
                    "Para luz linearmente polarizada vertical.",
                    "Para luz circularmente polarizada (RCP ou LCP).",
                    "Para luz não polarizada."
                },
                2,
                "Como o torque está ligado à helicidade da luz (I_RCP − I_LCP), ele é máximo "
                + "para luz circularmente polarizada e nulo para polarização linear."
            ),

            new Question(
                "15) Segundo o artigo, por que o campo magnético óptico não deve ser ignorado "
              + "em modelos de magneto-ótica ultrarrápida?",
                new String[]{
                    "Porque ele domina totalmente e substitui o campo elétrico óptico.",
                    "Porque ele altera o índice de refração linear em ordens de grandeza.",
                    "Porque ele introduz uma contribuição mensurável aos efeitos Faraday e "
                    + "inverso Faraday e ajuda a explicar discrepâncias experimentais.",
                    "Porque ele elimina a necessidade de considerar amortecimento na LLG."
                },
                2,
                "Embora não seja a única contribuição, o campo magnético óptico fornece um termo "
                + "mensurável na rotação Faraday e no IFE, e ajuda a reconciliar teoria e "
                + "experimento em regimes ultrarrápidos, por isso não deve ser negligenciado."
            )
        };
    }

    private void createUI() {
    setLayout(new BorderLayout());

    // ===== PAINEL DA PERGUNTA (FUNDO PRETO) =====
    questionArea = new JTextArea();
    questionArea.setEditable(false);
    questionArea.setLineWrap(true);
    questionArea.setWrapStyleWord(true);
    questionArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
    questionArea.setBackground(Color.BLACK);
    questionArea.setForeground(Color.WHITE);
    questionArea.setMargin(new java.awt.Insets(10, 10, 10, 10));

    JScrollPane scroll = new JScrollPane(questionArea);

    // Painel que vai conter a tela preta + pontuação no topo
    JPanel questionPanel = new JPanel(new BorderLayout());
    questionPanel.setBackground(Color.BLACK);
    questionPanel.add(scroll, BorderLayout.CENTER);

    // === PONTUAÇÃO NO CANTO SUPERIOR DIREITO DA TELA PRETA ===
    scoreLabel = new JLabel("Pontuação: 0");
    scoreLabel.setForeground(Color.WHITE);
    scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));

    questionPanel.add(scoreLabel, BorderLayout.NORTH);

    // adiciona o painel da pergunta (tela preta) ao centro da janela
    add(questionPanel, BorderLayout.CENTER);

    // ===== PAINEL DAS ALTERNATIVAS E STATUS (EMBAIXO) =====
    JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    optionButtons = new JButton[4];
    Font optionFont = new Font("SansSerif", Font.PLAIN, 16);

    for (int i = 0; i < 4; i++) {
        JButton btn = new JButton();
        btn.setFont(optionFont);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(230, 230, 230));
        int index = i;
        btn.addActionListener(e -> checkAnswer(index));
        optionButtons[i] = btn;
        optionsPanel.add(btn);
    }

    JPanel southPanel = new JPanel(new BorderLayout());
    southPanel.add(optionsPanel, BorderLayout.CENTER);

    // Barra de status (somente mensagem agora)
    JPanel statusPanel = new JPanel(new BorderLayout());
    statusLabel = new JLabel("Escolha uma alternativa.");
    statusPanel.add(statusLabel, BorderLayout.CENTER);
    southPanel.add(statusPanel, BorderLayout.NORTH);

    // Painel de botões inferiores (Próxima / Recomeçar / Sair)
JPanel bottomButtonsPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

// Botão Próxima
nextButton = new JButton("Próxima pergunta");
nextButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        goToNextQuestion();
    }
});
bottomButtonsPanel.add(nextButton);

// NOVO: Botão Recomeçar
restartButton = new JButton("Recomeçar");
restartButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        restartGame();
    }
});
// começa oculto
restartButton.setVisible(false);
bottomButtonsPanel.add(restartButton);

// NOVO: Botão Sair
exitButton = new JButton("Sair do jogo");
exitButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
});
// começa oculto
exitButton.setVisible(false);
bottomButtonsPanel.add(exitButton);

southPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);

add(southPanel, BorderLayout.SOUTH);

}


    // Carrega pergunta pelo índice
    private void loadQuestion(int index) {
        if (index < 0 || index >= questions.length) {
    // Fim do jogo
    questionArea.setText("Fim do quiz!\n\n"
            + "Jogador: " + playerName + "\n"
            + "Sua pontuação final foi: " + score + " de " + questions.length + " perguntas.");
    
    for (JButton btn : optionButtons) {
        btn.setEnabled(false);
        btn.setVisible(false);   // esconde os botões de alternativas
    }

    // Desativa "Próxima pergunta" e mostra os botões finais
    nextButton.setVisible(false);
    restartButton.setVisible(true);
    exitButton.setVisible(true);

    statusLabel.setText("Resultado final salvo. Escolha: Recomeçar ou Sair.");

    // Salva a pontuação no arquivo CSV
    saveScoreToFile();

    return;
}

        Question q = questions[index];
        questionArea.setText(q.text);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(q.options[i]);
            optionButtons[i].setEnabled(true);
        }
        answered = false;
        statusLabel.setText("Escolha uma alternativa.");
    }

    // Verifica resposta
    private void checkAnswer(int chosenIndex) {
        if (answered) return; // evita clicar duas vezes

        answered = true;
        Question q = questions[currentIndex];

        if (chosenIndex == q.correctIndex) {
            score++;
            statusLabel.setText("Correto!");
        } else {
            statusLabel.setText(
                "Incorreto. A alternativa correta é: " + (char)('A' + q.correctIndex));
        }

        // Mostra explicação no texto
        StringBuilder sb = new StringBuilder();
        sb.append(q.text).append("\n\n");
        char letraCorreta = (char) ('A' + q.correctIndex);
        sb.append("Resposta correta: ").append(letraCorreta).append(")\n\n");
        sb.append("Explicação:\n").append(q.explanation);
        questionArea.setText(sb.toString());

        // Atualiza pontuação
        scoreLabel.setText("Pontuação: " + score + " / " + questions.length);

        // Desabilita botões de opções até a próxima
        for (JButton btn : optionButtons) {
            btn.setEnabled(false);
        }
    }

    // Vai para a próxima pergunta
    private void goToNextQuestion() {
        if (!answered) {
            statusLabel.setText("Responda a pergunta antes de avançar.");
            return;
        }
        currentIndex++;
        loadQuestion(currentIndex);
    }
    
    private void restartGame() {
    // reseta estado
    score = 0;
    currentIndex = 0;
    answered = false;

    // reexibe e reabilita os botões de alternativas
    for (JButton btn : optionButtons) {
        btn.setVisible(true);
        btn.setEnabled(true);
    }

    // volta a mostrar o botão "Próxima" e esconde Recomeçar/Sair
    nextButton.setVisible(true);
    restartButton.setVisible(false);
    exitButton.setVisible(false);

    // atualiza textos
    scoreLabel.setText("Pontuação: 0 / " + questions.length);
    statusLabel.setText("Escolha uma alternativa.");

    // carrega a primeira pergunta de novo
    loadQuestion(0);
}


    // Salva pontuação em arquivo CSV
    private void saveScoreToFile() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try (PrintWriter out = new PrintWriter(new FileWriter(SCORE_FILE, true))) {
            // Cabeçalho opcional: você pode gerar antes se quiser
            // Formato: datahora;nome;pontos;total_perguntas
            out.printf("%s;%s;%d;%d%n", timestamp, playerName, score, questions.length);
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Não foi possível salvar a pontuação: " + e.getMessage());
        }
    }

    // Classe simples de pergunta
    private static class Question {
        String text;
        String[] options;
        int correctIndex;
        String explanation;

        Question(String text, String[] options, int correctIndex, String explanation) {
            this.text = text;
            this.options = options;
            this.correctIndex = correctIndex;
            this.explanation = explanation;
        }
    }
}
