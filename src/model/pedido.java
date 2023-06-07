/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author pc2
 */
public class pedido {
    
public pedido(){}    
    
   private String id_pedido;
   private String id_cliente;
   private String montante_aprovado;
   private String juros;
   private String prestacao;
   private String prazo_de_pagamento;
   private String valor_total;
   private byte[] archivopdf;

    public String getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getMontante_aprovado() {
        return montante_aprovado;
    }

    public void setMontante_aprovado(String montante_aprovado) {
        this.montante_aprovado = montante_aprovado;
    }

    public String getJuros() {
        return juros;
    }

    public void setJuros(String juros) {
        this.juros = juros;
    }

    public String getPrestacao() {
        return prestacao;
    }

    public void setPrestacao(String prestacao) {
        this.prestacao = prestacao;
    }

    public String getPrazo_de_pagamento() {
        return prazo_de_pagamento;
    }

    public void setPrazo_de_pagamento(String prazo_de_pagamento) {
        this.prazo_de_pagamento = prazo_de_pagamento;
    }

    public String getValor_total() {
        return valor_total;
    }

    public void setValor_total(String valor_total) {
        this.valor_total = valor_total;
    }

    public byte[] getArchivopdf() {
        return archivopdf;
    }

    public void setArchivopdf(byte[] archivopdf) {
        this.archivopdf = archivopdf;
    }

}
