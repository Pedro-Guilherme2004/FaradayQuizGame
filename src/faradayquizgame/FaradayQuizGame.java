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
 * Quiz sobre o artigo
 * "Faraday effects emerging from the optical magnetic field"
 * com suporte a Português / Español / English.
 */
public class FaradayQuizGame extends JFrame {

    // Idiomas suportados
    private enum Language {
        PT, ES, EN
    }

    private Language language = Language.PT;

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
        // Primeiro escolhe idioma
        askLanguage();

        setTitle(t("window_title"));
        setSize(880, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Pergunta o nome do jogador
        askPlayerName();

        // Cria perguntas de acordo com o idioma
        createQuestions();
        createUI();
        loadQuestion(0);
    }

    // ==== SELEÇÃO DE IDIOMA ====
    private void askLanguage() {
        String[] options = {"Português", "Español", "English"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Escolha o idioma / Elija el idioma / Choose the language:",
                "Idioma / Language",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choice) {
            case 1:
                language = Language.ES;
                break;
            case 2:
                language = Language.EN;
                break;
            default:
                language = Language.PT;
        }
    }

    // ==== TRADUÇÃO DE TEXTOS FIXOS DA INTERFACE ====
    private String t(String key) {
        switch (language) {
            case ES:
                switch (key) {
                    case "window_title": return "Quiz - Efecto Faraday y Campo Magnético Óptico";
                    case "enter_name": return "Escribe tu nombre (o apodo) para registrar la puntuación:";
                    case "name_dialog_title": return "Identificación del jugador";
                    case "default_name": return "Anónimo";
                    case "score_prefix": return "Puntuación";
                    case "status_choose": return "Elige una alternativa.";
                    case "status_answer_before_next": return "Responde la pregunta antes de avanzar.";
                    case "status_correct": return "¡Correcto!";
                    case "status_incorrect_prefix": return "Incorrecto. La alternativa correcta es: ";
                    case "final_header": return "Fin del quiz";
                    case "final_player": return "Jugador";
                    case "final_score_line": return "Tu puntuación final fue: %d de %d preguntas.";
                    case "final_saved": return "Resultado guardado. Elige: Reiniciar o Salir.";
                    case "button_next": return "Siguiente pregunta";
                    case "button_restart": return "Reiniciar";
                    case "button_exit": return "Salir del juego";
                    case "save_error_prefix": return "No fue posible guardar la puntuación: ";
                    case "correct_answer_prefix": return "Respuesta correcta: ";
                    case "explanation_prefix": return "Explicación:\n";
                }
                break;

            case EN:
                switch (key) {
                    case "window_title": return "Quiz - Faraday Effect & Optical Magnetic Field";
                    case "enter_name": return "Enter your name (or nickname) to save the score:";
                    case "name_dialog_title": return "Player identification";
                    case "default_name": return "Anonymous";
                    case "score_prefix": return "Score";
                    case "status_choose": return "Choose an option.";
                    case "status_answer_before_next": return "Answer the question before going on.";
                    case "status_correct": return "Correct!";
                    case "status_incorrect_prefix": return "Incorrect. The correct option is: ";
                    case "final_header": return "End of quiz";
                    case "final_player": return "Player";
                    case "final_score_line": return "Your final score was: %d out of %d questions.";
                    case "final_saved": return "Result saved. Choose: Restart or Exit.";
                    case "button_next": return "Next question";
                    case "button_restart": return "Restart";
                    case "button_exit": return "Exit game";
                    case "save_error_prefix": return "Could not save score: ";
                    case "correct_answer_prefix": return "Correct answer: ";
                    case "explanation_prefix": return "Explanation:\n";
                }
                break;

            case PT:
            default:
                switch (key) {
                    case "window_title": return "Quiz - Efeito Faraday & Campo Magnético Óptico";
                    case "enter_name": return "Digite seu nome (ou apelido) para registrar a pontuação:";
                    case "name_dialog_title": return "Identificação do jogador";
                    case "default_name": return "Anônimo";
                    case "score_prefix": return "Pontuação";
                    case "status_choose": return "Escolha uma alternativa.";
                    case "status_answer_before_next": return "Responda a pergunta antes de avançar.";
                    case "status_correct": return "Correto!";
                    case "status_incorrect_prefix": return "Incorreto. A alternativa correta é: ";
                    case "final_header": return "Fim do quiz";
                    case "final_player": return "Jogador";
                    case "final_score_line": return "Sua pontuação final foi: %d de %d perguntas.";
                    case "final_saved": return "Resultado final salvo. Escolha: Recomeçar ou Sair.";
                    case "button_next": return "Próxima pergunta";
                    case "button_restart": return "Recomeçar";
                    case "button_exit": return "Sair do jogo";
                    case "save_error_prefix": return "Não foi possível salvar a pontuação: ";
                    case "correct_answer_prefix": return "Resposta correta: ";
                    case "explanation_prefix": return "Explicação:\n";
                }
        }
        return key; // fallback
    }

    // Pergunta o nome do aluno/colega
    private void askPlayerName() {
        playerName = JOptionPane.showInputDialog(
                this,
                t("enter_name"),
                t("name_dialog_title"),
                JOptionPane.QUESTION_MESSAGE
        );
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = t("default_name");
        }
    }

    // Decide qual conjunto de perguntas usar
    private void createQuestions() {
        switch (language) {
            case ES:
                questions = createQuestionsEs();
                break;
            case EN:
                questions = createQuestionsEn();
                break;
            case PT:
            default:
                questions = createQuestionsPt();
        }
    }

    // ===================== PERGUNTAS EM PORTUGUÊS =====================
    private Question[] createQuestionsPt() {
        return new Question[]{

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

    // ===================== PERGUNTAS EM ESPANHOL =====================
    private Question[] createQuestionsEs() {
        return new Question[]{

            new Question(
                "1) ¿Cuál es la idea principal del artículo con respecto al campo magnético óptico?",
                new String[]{
                    "Que el campo magnético de la luz es despreciable en magneto-óptica.",
                    "Que solo el campo eléctrico óptico contribuye al efecto Faraday.",
                    "Que el campo magnético óptico también contribuye al efecto Faraday y al efecto Faraday inverso.",
                    "Que el efecto Faraday no existe en materiales paramagnéticos."
                },
                2,
                "El artículo muestra que el componente magnético de la luz no es despreciable: "
                + "genera un torque vía la ecuación de Landau-Lifshitz-Gilbert y contribuye tanto "
                + "al efecto Faraday inverso (IFE) como al Faraday directo (FE)."
            ),

            new Question(
                "2) ¿Qué es el efecto Faraday (FE) clásico descrito en el artículo?",
                new String[]{
                    "Rotación de la polarización debida a un campo eléctrico estático.",
                    "Rotación de la polarización causada por un campo magnético estático que induce birrefringencia circular.",
                    "Emisión de fotones por un imán calentado.",
                    "Solo el aumento de la intensidad de la luz en un medio magnético."
                },
                1,
                "El FE es la rotación del plano de polarización de un haz óptico cuando se propaga "
                + "en un material bajo un campo magnético estático, debido a la birrefringencia circular inducida."
            ),

            new Question(
                "3) ¿Qué es el efecto Faraday inverso (IFE) en la visión tradicional de Pershan?",
                new String[]{
                    "Una rotación de polarización generada por un campo magnético estático.",
                    "Una magnetización inducida por el campo eléctrico óptico en un proceso no lineal.",
                    "Un efecto puramente térmico sin dependencia de la polarización.",
                    "Un fenómeno que ocurre solo con luz linealmente polarizada."
                },
                1,
                "En la formulación de Pershan, el IFE es una magnetización inducida por el campo eléctrico óptico "
                + "a través de una susceptibilidad magneto-óptica no lineal de segundo orden."
            ),

            new Question(
                "4) ¿Qué ecuación de dinámica de espines usa el artículo para describir el torque "
              + "del campo magnético óptico?",
                new String[]{
                    "Ecuación de Schrödinger independiente del tiempo.",
                    "Ecuaciones de Maxwell en forma integral.",
                    "Ecuación de Landau-Lifshitz-Gilbert (LLG).",
                    "Ecuación de Navier-Stokes."
                },
                2,
                "El artículo usa la ecuación de Landau-Lifshitz-Gilbert (LLG), que describe la precesión "
                + "y el amortiguamiento del vector de magnetización bajo campos magnéticos efectivos."
            ),

            new Question(
                "5) La razón adimensional η = αγ H_opt / f_opt, introducida en el artículo, "
              + "¿se relaciona con qué magnitud física?",
                new String[]{
                    "Con la razón entre campo eléctrico y campo magnético.",
                    "Con la fuerza de interacción entre el campo magnético óptico y la magnetización.",
                    "Con la temperatura del material.",
                    "Con el índice de refracción del medio."
                },
                1,
                "η cuantifica la fuerza del torque inducido por el campo magnético óptico en la LLG, "
                + "dependiendo del amortiguamiento de Gilbert α, de la amplitud H_opt y de la frecuencia óptica f_opt."
            ),

            new Question(
                "6) En el artículo, el torque longitudinal Tz generado por un pulso circularmente polarizado "
              + "presenta qué dependencia con la fluencia óptica F?",
                new String[]{
                    "Tz es independiente de F.",
                    "Tz es proporcional a la raíz cuadrada de F.",
                    "Tz es inversamente proporcional a F.",
                    "Tz es lineal en F (Tz ∝ F)."
                },
                3,
                "Las simulaciones muestran que Tz crece linealmente con la fluencia F, ya que Tz ∝ H_peak^2 · τ_p, "
                + "y la fluencia es proporcional a H_peak^2 · τ_p."
            ),

            new Question(
                "7) Para un haz con polarización general, el artículo muestra que el torque longitudinal Tz "
              + "es proporcional a qué combinación de intensidades?",
                new String[]{
                    "A la suma I_RCP + I_LCP.",
                    "A la diferencia I_RCP − I_LCP.",
                    "Solo a la intensidad de la componente lineal horizontal.",
                    "Solo a la intensidad de la componente lineal vertical."
                },
                1,
                "El torque longitudinal es máximo para luz circularmente polarizada y nulo para luz lineal; "
                + "matemáticamente Tz ∝ I_RCP − I_LCP."
            ),

            new Question(
                "8) ¿Qué concluye el artículo sobre la contribución del campo magnético óptico al "
              + "constante de Verdet del cristal TGG a 800 nm?",
                new String[]{
                    "Es despreciable, menor que el 1% del valor medido.",
                    "Responde aproximadamente por el 5% del valor medido.",
                    "Responde por alrededor del 17–18% del valor medido.",
                    "Explica totalmente (100%) el valor medido."
                },
                2,
                "Con los parámetros del TGG, el modelo vía LLG proporciona un Verdet que corresponde a "
                + "≈17,5% del valor experimental en 800 nm; el resto proviene de mecanismos ligados al campo eléctrico óptico."
            ),

            new Question(
                "9) Según el artículo, ¿qué ocurre con la relación de reciprocidad entre el "
              + "efecto Faraday (FE) y el efecto Faraday inverso (IFE) en escalas de tiempo ultrarrápidas?",
                new String[]{
                    "La reciprocidad se mantiene rigurosamente.",
                    "La reciprocidad se rompe: FE e IFE no pueden describirse con el mismo Verdet.",
                    "El FE deja de existir en esas escalas.",
                    "El IFE se vuelve puramente térmico."
                },
                1,
                "Los autores muestran que, en el régimen fuera de equilibrio (pulso ultrarrápido), "
                + "los constantes de Verdet derivados para FE e IFE a partir de la LLG son diferentes, "
                + "lo que refleja la ruptura de reciprocidad observada en experimentos."
            ),

            new Question(
                "10) ¿Por qué la contribución del campo magnético óptico al FE se vuelve relativamente "
              + "más importante en longitudes de onda largas (≈1,3 µm)?",
                new String[]{
                    "Porque el índice de refracción tiende a cero.",
                    "Porque el Verdet asociado al campo eléctrico disminuye al aumentar λ, "
                    + "mientras que la parte de origen magnético es aproximadamente independiente de λ.",
                    "Porque el campo magnético óptico desaparece a altas frecuencias.",
                    "Porque el TGG deja de ser transparente en esa región espectral."
                },
                1,
                "Los datos experimentales muestran que |V| total disminuye con λ, mientras que el término calculado "
                + "vía LLG ligado al campo magnético es casi constante; por eso, la fracción relativa de esa "
                + "contribución crece para longitudes de onda mayores."
            ),

            new Question(
                "11) ¿En qué tipo de material implementa numéricamente el artículo el modelo del "
              + "campo magnético óptico?",
                new String[]{
                    "En un metal ferromagnético opaco.",
                    "En un aislante diamagnético transparente.",
                    "En un cristal paramagnético transparente de TGG (granate gadolinio-galio).",
                    "En un semiconductor dopado con electrones libres."
                },
                2,
                "Los autores usan como caso de estudio el TGG (terbium gallium garnet), "
                + "un cristal paramagnético transparente muy usado en dispositivos Faraday."
            ),

            new Question(
                "12) En la LLG usada en el artículo, el término de amortiguamiento con constante α "
              + "tiene qué efecto principal sobre la magnetización?",
                new String[]{
                    "Hace que la magnetización crezca indefinidamente.",
                    "Causa precesión sin cambio de módulo.",
                    "Relaja la magnetización hacia el campo efectivo, disipando energía.",
                    "Invierte instantáneamente el signo de la magnetización."
                },
                2,
                "El término de amortiguamiento de Gilbert con parámetro α hace que la magnetización "
                + "se relaje hacia el campo efectivo, representando la disipación de energía del sistema de espines."
            ),

            new Question(
                "13) El artículo discute que el campo magnético óptico puede ser especialmente relevante "
              + "¿en qué régimen temporal?",
                new String[]{
                    "Régimen estacionario de onda continua (CW) y tiempos largos.",
                    "Régimen de pulsos ultracortos, en la escala de femtosegundos.",
                    "Régimen DC (campos constantes).",
                    "Régimen puramente térmico, sin oscilación óptica."
                },
                1,
                "La contribución magnética se analiza para pulsos ultracortos (fs), donde la respuesta "
                + "transitoria de la magnetización es importante y la reciprocidad FE/IFE puede romperse."
            ),

            new Question(
                "14) En relación con la polarización de la luz incidente, ¿en qué caso el torque longitudinal "
              + "Tz calculado es máximo?",
                new String[]{
                    "Para luz linealmente polarizada horizontal.",
                    "Para luz linealmente polarizada vertical.",
                    "Para luz circularmente polarizada (RCP o LCP).",
                    "Para luz no polarizada."
                },
                2,
                "Como el torque está ligado a la helicidad de la luz (I_RCP − I_LCP), es máximo "
                + "para luz circularmente polarizada y nulo para polarización lineal."
            ),

            new Question(
                "15) Según el artículo, ¿por qué el campo magnético óptico no debe ignorarse "
              + "en modelos de magneto-óptica ultrarrápida?",
                new String[]{
                    "Porque domina totalmente y sustituye al campo eléctrico óptico.",
                    "Porque cambia el índice de refracción lineal en órdenes de magnitud.",
                    "Porque introduce una contribución medible a los efectos Faraday y "
                    + "Faraday inverso y ayuda a explicar discrepancias experimentales.",
                    "Porque elimina la necesidad de considerar amortiguamiento en la LLG."
                },
                2,
                "Aunque no es la única contribución, el campo magnético óptico proporciona un término "
                + "medible en la rotación Faraday y en el IFE, y ayuda a reconciliar teoría y "
                + "experimento en regímenes ultrarrápidos."
            )
        };
    }

    // ===================== PERGUNTAS EM INGLÊS =====================
    private Question[] createQuestionsEn() {
        return new Question[]{

            new Question(
                "1) What is the main idea of the paper regarding the optical magnetic field?",
                new String[]{
                    "That the magnetic field of light is negligible in magneto-optics.",
                    "That only the optical electric field contributes to the Faraday effect.",
                    "That the optical magnetic field also contributes to the Faraday and inverse Faraday effects.",
                    "That the Faraday effect does not exist in paramagnetic materials."
                },
                2,
                "The paper shows that the magnetic component of light is not negligible: "
                + "it generates torque via the Landau-Lifshitz-Gilbert equation and contributes "
                + "to both the inverse Faraday effect (IFE) and the direct Faraday effect (FE)."
            ),

            new Question(
                "2) What is the classical Faraday effect (FE) described in the paper?",
                new String[]{
                    "Rotation of polarization due to a static electric field.",
                    "Rotation of polarization caused by a static magnetic field that induces circular birefringence.",
                    "Emission of photons from a heated magnet.",
                    "Only an increase in light intensity in a magnetic medium."
                },
                1,
                "The FE is the rotation of the plane of polarization of a light beam when it "
                + "propagates in a material under a static magnetic field, due to induced circular birefringence."
            ),

            new Question(
                "3) In Pershan’s traditional view, what is the inverse Faraday effect (IFE)?",
                new String[]{
                    "A polarization rotation generated by a static magnetic field.",
                    "A magnetization induced by the optical electric field in a nonlinear process.",
                    "A purely thermal effect with no polarization dependence.",
                    "A phenomenon that occurs only with linearly polarized light."
                },
                1,
                "In Pershan’s formulation, the IFE is a magnetization induced by the optical electric field "
                + "through a second-order nonlinear magneto-optical susceptibility."
            ),

            new Question(
                "4) Which spin dynamics equation is used in the paper to describe the torque from "
              + "the optical magnetic field?",
                new String[]{
                    "The time-independent Schrödinger equation.",
                    "Maxwell's equations in integral form.",
                    "The Landau-Lifshitz-Gilbert (LLG) equation.",
                    "The Navier-Stokes equation."
                },
                2,
                "The paper uses the Landau-Lifshitz-Gilbert (LLG) equation, which describes the precession "
                + "and damping of the magnetization vector under effective magnetic fields."
            ),

            new Question(
                "5) The dimensionless ratio η = αγ H_opt / f_opt introduced in the paper is related to what?",
                new String[]{
                    "To the ratio between electric and magnetic fields.",
                    "To the strength of the interaction between the optical magnetic field and the magnetization.",
                    "To the temperature of the material.",
                    "To the refractive index of the medium."
                },
                1,
                "η quantifies the strength of the torque induced by the optical magnetic field in LLG, "
                + "depending on the Gilbert damping α, the field amplitude H_opt, and the optical frequency f_opt."
            ),

            new Question(
                "6) In the paper, the longitudinal torque Tz generated by a circularly polarized pulse "
              + "shows what dependence on the optical fluence F?",
                new String[]{
                    "Tz is independent of F.",
                    "Tz is proportional to the square root of F.",
                    "Tz is inversely proportional to F.",
                    "Tz is linear in F (Tz ∝ F)."
                },
                3,
                "The simulations show that Tz grows linearly with fluence F, because Tz ∝ H_peak^2 · τ_p "
                + "and the fluence is proportional to H_peak^2 · τ_p."
            ),

            new Question(
                "7) For a beam with general polarization, the paper shows that the longitudinal torque Tz "
              + "is proportional to which combination of intensities?",
                new String[]{
                    "To the sum I_RCP + I_LCP.",
                    "To the difference I_RCP − I_LCP.",
                    "Only to the intensity of the horizontal linear component.",
                    "Only to the intensity of the vertical linear component."
                },
                1,
                "The longitudinal torque is maximum for circularly polarized light and zero for linear light; "
                + "mathematically Tz ∝ I_RCP − I_LCP."
            ),

            new Question(
                "8) What does the paper conclude about the contribution of the optical magnetic field to the "
              + "Verdet constant of TGG at 800 nm?",
                new String[]{
                    "It is negligible, less than 1% of the measured value.",
                    "It accounts for roughly 5% of the measured value.",
                    "It accounts for about 17–18% of the measured value.",
                    "It explains 100% of the measured value."
                },
                2,
                "With the parameters of TGG, the LLG-based model provides a Verdet constant corresponding to "
                + "≈17.5% of the experimental value at 800 nm; the remaining part comes from mechanisms "
                + "related to the optical electric field."
            ),

            new Question(
                "9) According to the paper, what happens to the reciprocity relation between the "
              + "Faraday effect (FE) and the inverse Faraday effect (IFE) on ultrafast time scales?",
                new String[]{
                    "Reciprocity is strictly preserved.",
                    "Reciprocity breaks down: FE and IFE cannot be described by the same Verdet constant.",
                    "The FE ceases to exist on these time scales.",
                    "The IFE becomes purely thermal."
                },
                1,
                "The authors show that, in the out-of-equilibrium (ultrafast) regime, the Verdet constants "
                + "derived for FE and IFE from LLG are different, which reflects the observed breakdown "
                + "of reciprocity in experiments."
            ),

            new Question(
                "10) Why does the contribution of the optical magnetic field to FE become relatively "
              + "more important at long wavelengths (≈1.3 µm)?",
                new String[]{
                    "Because the refractive index tends to zero.",
                    "Because the Verdet constant associated with the electric field decreases as λ increases, "
                    + "while the magnetic contribution is approximately independent of λ.",
                    "Because the optical magnetic field vanishes at high frequencies.",
                    "Because TGG is no longer transparent in that spectral region."
                },
                1,
                "Experimental data show that the total |V| decreases with λ, whereas the LLG-based term "
                + "associated with the magnetic field is nearly constant; therefore, its relative fraction "
                + "grows at longer wavelengths."
            ),

            new Question(
                "11) In what type of material does the paper numerically implement the optical magnetic "
              + "field model?",
                new String[]{
                    "In an opaque ferromagnetic metal.",
                    "In a transparent diamagnetic insulator.",
                    "In a transparent paramagnetic TGG crystal (terbium gallium garnet).",
                    "In a semiconductor doped with free electrons."
                },
                2,
                "The authors use TGG (terbium gallium garnet) as a case study, "
                + "a transparent paramagnetic crystal widely used in Faraday devices."
            ),

            new Question(
                "12) In the LLG equation used in the paper, the damping term with constant α has what "
              + "main effect on the magnetization?",
                new String[]{
                    "It makes the magnetization grow indefinitely.",
                    "It causes precession without changing the magnitude.",
                    "It relaxes the magnetization towards the effective field, dissipating energy.",
                    "It instantly flips the sign of the magnetization."
                },
                2,
                "The Gilbert damping term with parameter α causes the magnetization to relax toward "
                + "the effective field, representing energy dissipation in the spin system."
            ),

            new Question(
                "13) The paper discusses that the optical magnetic field can be especially relevant "
              + "in which temporal regime?",
                new String[]{
                    "Steady-state CW (continuous-wave) regime and long times.",
                    "Ultrashort pulse regime on the femtosecond time scale.",
                    "DC regime (static fields).",
                    "Purely thermal regime with no optical oscillation."
                },
                1,
                "The magnetic contribution is analyzed for ultrashort (fs) pulses, where the transient "
                + "magnetization response is important and FE/IFE reciprocity can break down."
            ),

            new Question(
                "14) Regarding the polarization of the incident light, in which case is the calculated "
              + "longitudinal torque Tz maximal?",
                new String[]{
                    "For horizontally linearly polarized light.",
                    "For vertically linearly polarized light.",
                    "For circularly polarized light (RCP or LCP).",
                    "For unpolarized light."
                },
                2,
                "Since the torque is linked to the light’s helicity (I_RCP − I_LCP), it is maximal "
                + "for circularly polarized light and zero for linear polarization."
            ),

            new Question(
                "15) According to the paper, why should the optical magnetic field not be ignored "
              + "in ultrafast magneto-optical models?",
                new String[]{
                    "Because it completely dominates and replaces the optical electric field.",
                    "Because it changes the linear refractive index by orders of magnitude.",
                    "Because it introduces a measurable contribution to the Faraday and inverse Faraday effects "
                    + "and helps explain experimental discrepancies.",
                    "Because it removes the need to consider damping in LLG."
                },
                2,
                "Although it is not the only contribution, the optical magnetic field provides a measurable term "
                + "in Faraday rotation and IFE, and helps reconcile theory and experiment in ultrafast regimes."
            )
        };
    }

    // ===================== UI / LÓGICA =====================
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

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(Color.BLACK);
        questionPanel.add(scroll, BorderLayout.CENTER);

        // Pontuação no topo direito
        String scorePrefix = t("score_prefix");
        scoreLabel = new JLabel(scorePrefix + ": 0 / " + questions.length);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));

        questionPanel.add(scoreLabel, BorderLayout.NORTH);
        add(questionPanel, BorderLayout.CENTER);

        // ===== PAINEL DAS ALTERNATIVAS E STATUS =====
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

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel(t("status_choose"));
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        southPanel.add(statusPanel, BorderLayout.NORTH);

        // Botões inferiores
        JPanel bottomButtonsPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        nextButton = new JButton(t("button_next"));
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToNextQuestion();
            }
        });
        bottomButtonsPanel.add(nextButton);

        restartButton = new JButton(t("button_restart"));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        restartButton.setVisible(false);
        bottomButtonsPanel.add(restartButton);

        exitButton = new JButton(t("button_exit"));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitButton.setVisible(false);
        bottomButtonsPanel.add(exitButton);

        southPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    // Carrega pergunta
    private void loadQuestion(int index) {
        if (index < 0 || index >= questions.length) {
            // Fim do jogo
            StringBuilder sb = new StringBuilder();
            sb.append(t("final_header")).append("!\n\n")
              .append(t("final_player")).append(": ").append(playerName).append("\n")
              .append(String.format(t("final_score_line"), score, questions.length));

            questionArea.setText(sb.toString());

            for (JButton btn : optionButtons) {
                btn.setEnabled(false);
                btn.setVisible(false);
            }

            nextButton.setVisible(false);
            restartButton.setVisible(true);
            exitButton.setVisible(true);

            statusLabel.setText(t("final_saved"));

            saveScoreToFile();
            return;
        }

        Question q = questions[index];
        questionArea.setText(q.text);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(q.options[i]);
            optionButtons[i].setEnabled(true);
            optionButtons[i].setVisible(true);
        }
        answered = false;
        statusLabel.setText(t("status_choose"));
    }

    // Verifica resposta
    private void checkAnswer(int chosenIndex) {
        if (answered) return;

        answered = true;
        Question q = questions[currentIndex];

        if (chosenIndex == q.correctIndex) {
            score++;
            statusLabel.setText(t("status_correct"));
        } else {
            statusLabel.setText(
                t("status_incorrect_prefix") + (char)('A' + q.correctIndex));
        }

        // Monta explicação
        StringBuilder sb = new StringBuilder();
        sb.append(q.text).append("\n\n");
        char letraCorreta = (char) ('A' + q.correctIndex);
        sb.append(t("correct_answer_prefix")).append(letraCorreta).append(")\n\n");
        sb.append(t("explanation_prefix")).append(q.explanation);
        questionArea.setText(sb.toString());

        // Atualiza pontuação
        scoreLabel.setText(t("score_prefix") + ": " + score + " / " + questions.length);

        for (JButton btn : optionButtons) {
            btn.setEnabled(false);
        }
    }

    // Próxima pergunta
    private void goToNextQuestion() {
        if (!answered) {
            statusLabel.setText(t("status_answer_before_next"));
            return;
        }
        currentIndex++;
        loadQuestion(currentIndex);
    }

    // Recomeçar
    private void restartGame() {
        score = 0;
        currentIndex = 0;
        answered = false;

        for (JButton btn : optionButtons) {
            btn.setVisible(true);
            btn.setEnabled(true);
        }

        nextButton.setVisible(true);
        restartButton.setVisible(false);
        exitButton.setVisible(false);

        scoreLabel.setText(t("score_prefix") + ": 0 / " + questions.length);
        statusLabel.setText(t("status_choose"));

        loadQuestion(0);
    }

    // Salva pontuação em CSV
    private void saveScoreToFile() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try (PrintWriter out = new PrintWriter(new FileWriter(SCORE_FILE, true))) {
            out.printf("%s;%s;%d;%d;%s%n",
                    timestamp, playerName, score, questions.length, language.name());
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText(t("save_error_prefix") + e.getMessage());
        }
    }

    // Classe de pergunta
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
