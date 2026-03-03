



data class Requisito(

    val mensagemErro: String,
    val validacao: (String) -> Boolean

)

val personagensTime7 = listOf("Naruto", "Sasuke", "Sakura", "Kakashi")

val regras = listOf(

    Requisito("❌ A senha deve ter no minímo 5 caracteres! ❌") { senha -> senha.length >= 5 },
    Requisito("❌ A senha deve conter o ano que o Brasil será hexa!! ❌") { senha -> senha.contains("2026")},
    Requisito("❌ A senha deve conter NO MINIMO 1 letra maiuscula ❌ e 1 número") { senha -> senha.any { it.isUpperCase() } && senha.any {it.isDigit()}},
    Requisito("❌ A senha deve conter um ingrediente de bolo de chocolate ❌") { senha -> senha.contains("leite", ignoreCase = true) || senha.contains("açucar", ignoreCase = true) || senha.contains("ovo", ignoreCase = true) || senha.contains("chocolate", ignoreCase = true) || senha.contains("farinha", ignoreCase = true)},
    Requisito("❌ A senha deve conter um emoji/caracter especial ❌") { senha -> senha.any {char -> !char.isDigit() && !char.isLetter() && !char.isWhitespace()}},
    Requisito("❌ A senha deve conter pelo menos 1 personagem do time 7 de naruto ❌") { senha -> senha.any {personagensTime7.any { personagem -> senha.contains(personagem, ignoreCase = true) }}},
    Requisito("❌ A senha deve conter o nome do maior time de SC ❌") { senha -> senha.contains("criciúma", ignoreCase = true) }
)

fun main() {

    var senhaUsuario: String
    var erroEncontrado: Requisito?

    do {

        println("Digite sua senha")
        senhaUsuario = readln()

        erroEncontrado = regras.find { regra -> !regra.validacao(senhaUsuario) }

        if (erroEncontrado !=null ){
            println(erroEncontrado.mensagemErro)
        }

    } while(erroEncontrado != null)

    println("🎉 Parabéns, melhor senha de todas criada!!!\n\n" + "Senha final: " + senhaUsuario)

}






