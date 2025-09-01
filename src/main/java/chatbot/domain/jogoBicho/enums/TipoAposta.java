package chatbot.domain.jogoBicho.enums;

public enum TipoAposta {
    GRUPO("Grupo", "Aposta simples em grupo de animais", 1),
    DUQUE_GRUPO("Duque de Grupo", "Aposta em 2 grupos de animais", 2),
    DUQUE_DEZENA("Duque de Dezena", "Aposta em 2 dezenas", 3),
    TERNO_GRUPO("Terno de Grupo", "Aposta em 3 grupos de animais", 4),
    TERNO_DEZENA("Terno de Dezena", "Aposta em 3 dezenas", 5),
    QUADRA_GRUPO("Quadra de Grupo", "Aposta em 4 grupos de animais", 6),
    QUINA_GRUPO("Quina de Grupo", "Aposta em 5 grupos de animais", 7),
    CENTENA("Centena", "Aposta em centena (3 números)", 8),
    MILHAR("Milhar", "Aposta em milhar (4 números)", 9),
    PASSE("Passe", "Aposta em grupo com passe", 10);

    private final String nome;
    private final String descricao;
    private final int codigo;

    TipoAposta(String nome, String descricao, int codigo) {
        this.nome = nome;
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoAposta fromCodigo(int codigo) {
        for (TipoAposta tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        return null;
    }
}