[![](https://jitpack.io/v/uJ0hn/sRconGlobal.svg)](https://jitpack.io/#uJ0hn/sRconGlobal)
# sRconGlobal
Library para o [Source RCON Protocol](https://developer.valvesoftware.com/wiki/Source_RCON_Protocol), otimizado para conexões minecraft.
## Uso
```java
// Conectar ao ip 127.0.0.1 com a porta 25565
try {  
    rcon = new Rcon("127.0.0.1", 25565, "senha".getBytes());  
} catch (IOException | AuthenticationException e) {  
    throw new RuntimeException(e);  
}

// Exemplo, dentro do servidor minecraft para ver a lista de jogadores online
String resultado = rcon.comando("list");

// Agora enviar ao console o resultado
System.out.println(resultado);
```
Ao se conectar ao servidor rcon, um `AuthenticationException` será lançado se a senha estiver incorreta.
## Download
O mais recente .jar estará dentro do  [Projeto/releases](https://github.com/uJ0hn/sRconGlobal/releases) pronto para ser utilizado
## Maven
```xml
//pom.xml
<dependency>  
    <groupId>com.github.uJ0hn</groupId>  
    <artifactId>sRconGlobal</artifactId>  
    <version>versão</version>  
</dependency>

```
