package com.example.imdmarket

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AlterarProdutoActivity : AppCompatActivity() {
    private lateinit var produtoDAO: ProdutoDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alterar_produto)

        // Configurar padding dinâmico
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar DAO
        produtoDAO = ProdutoDAO(this)

        // Referências aos campos e botões
        val etCodigo = findViewById<EditText>(R.id.etCodigo)
        val etNome = findViewById<EditText>(R.id.etNome)
        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        val etEstoque = findViewById<EditText>(R.id.etEstoque)
        val btnAlterar = findViewById<Button>(R.id.btnAlterar)
        val btnLimpar = findViewById<Button>(R.id.btnLimpar)

        // Botão "Alterar"
        btnAlterar.setOnClickListener {
            val codigo = etCodigo.text.toString()
            val nome = etNome.text.toString()
            val descricao = etDescricao.text.toString()
            val estoque = etEstoque.text.toString()

            // Validação simples
            if (codigo.isEmpty() || nome.isEmpty() || descricao.isEmpty() || estoque.isEmpty()) {
                Toast.makeText(this, "Todos os campos são obrigatórios!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar se o produto existe
            val produtos = produtoDAO.listarProdutos()
            val produtoExistente = produtos.find { it.codigo == codigo }

            if (produtoExistente == null) {
                Toast.makeText(this, "Produto não encontrado!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Atualizar o produto
            val produtoAtualizado = Produto(codigo, nome, descricao, estoque.toInt())
            val sucesso = produtoDAO.atualizarProduto(produtoAtualizado)

            if (sucesso) {
                Toast.makeText(this, "Produto alterado com sucesso!", Toast.LENGTH_SHORT).show()
                limparCampos(etCodigo, etNome, etDescricao, etEstoque)
            } else {
                Toast.makeText(this, "Erro ao alterar o produto!", Toast.LENGTH_SHORT).show()
            }
        }

        // Botão "Limpar"
        btnLimpar.setOnClickListener {
            limparCampos(etCodigo, etNome, etDescricao, etEstoque)
        }
    }

    private fun limparCampos(vararg campos: EditText) {
        campos.forEach { it.text.clear() }
    }
}
