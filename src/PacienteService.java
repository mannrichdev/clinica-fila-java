import com.sun.net.httpserver.HttpExchange;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class PacienteService {

    // Fila FIFO
    private static Queue<Paciente> pacientes = new LinkedList<>();

    // Adiciona paciente no fim da fila
    public static void adicionarPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    // Lista pacientes na ordem de chegada
    public static List<Paciente> listarPacientesFIFO() {
        return new ArrayList<>(pacientes);
    }

    // Atende (remove) o primeiro paciente da fila
    public static Paciente atenderPaciente() {
        return pacientes.poll();
    }

    public static Paciente criarPacienteDoFormulario(HttpExchange exchange) throws Exception {

        String corpo = lerCorpoDaRequisicao(exchange);

        String nome = obterValor(corpo, "nome");
        int idade = Integer.parseInt(obterValor(corpo, "idade"));
        String peso = obterValor(corpo, "peso");
        String altura = obterValor(corpo, "altura");
        String pressao = obterValor(corpo, "pressao");
        String temperatura = obterValor(corpo, "temperatura");
        String observacoes = obterValor(corpo, "observacoes");
        String rua = obterValor(corpo, "rua");
        String numero = obterValor(corpo, "numero");
        String bairro = obterValor(corpo, "bairro");
        String cidade = obterValor(corpo, "cidade");
        String uf = obterValor(corpo, "uf");

        return new Paciente(
                nome,
                idade,
                peso,
                altura,
                pressao,
                temperatura,
                observacoes,
                rua,
                numero,
                bairro,
                cidade,
                uf
        );
    }

    private static String lerCorpoDaRequisicao(HttpExchange exchange) throws Exception {

        InputStream entrada = exchange.getRequestBody();

        byte[] bytes = entrada.readAllBytes();

        return new String(bytes, "UTF-8");
    }

    private static String obterValor(String corpo, String campo) throws Exception {

        String[] partes = corpo.split("&");

        for (String parte : partes) {

            String[] chaveValor = parte.split("=");

            if (chaveValor.length == 2) {

                String chave = URLDecoder.decode(chaveValor[0], "UTF-8");

                String valor = URLDecoder.decode(chaveValor[1], "UTF-8");

                if (chave.equals(campo)) {
                    return valor;
                }
            }
        }

        return "";
    }
}