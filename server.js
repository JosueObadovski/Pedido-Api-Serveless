require("dotenv").config()
const express = require("express")
const mysql = require("mysql2/promise")
const serverless = require("serverless-http")

const app = express()
app.use(express.json())

const pool = mysql.createPool({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASS,
  database: process.env.DB_NAME,
  port: process.env.DB_PORT,
})

// Criar Pedido
app.post("/pedidos", async (req, res) => {
  const { cliente, email, itens } = req.body
  const total = itens.reduce(
    (acc, item) => acc + item.preco * item.quantidade,
    0
  )

  try {
    const conn = await pool.getConnection()
    const [result] = await conn.query(
      "INSERT INTO pedidos (cliente, email, total, status) VALUES (?, ?, ?, 'PENDENTE')",
      [cliente, email, total]
    )
    conn.release()

    res.status(201).json({ id: result.insertId, status: "PENDENTE", total })
  } catch (error) {
    console.error(error)
    res.status(500).json({ error: "Erro ao criar pedido" })
  }
})

// Listar Pedidos
app.get("/pedidos", async (req, res) => {
  try {
    const conn = await pool.getConnection()
    const [rows] = await conn.query("SELECT * FROM pedidos")
    conn.release()
    res.json(rows)
  } catch (error) {
    console.error(error)
    res.status(500).json({ error: "Erro ao buscar pedidos" })
  }
})

// Detalhes de um Pedido
app.get("/pedidos/:id", async (req, res) => {
  const { id } = req.params

  try {
    const conn = await pool.getConnection()
    const [rows] = await conn.query("SELECT * FROM pedidos WHERE id = ?", [id])
    conn.release()

    if (rows.length === 0) {
      return res.status(404).json({ error: "Pedido não encontrado" })
    }

    res.json(rows[0])
  } catch (error) {
    console.error(error)
    res.status(500).json({ error: "Erro ao buscar pedido" })
  }
})

// Atualizar Status do Pedido
app.patch("/pedidos/:id", async (req, res) => {
  const { id } = req.params
  const { status } = req.body

  try {
    const conn = await pool.getConnection()
    const [result] = await conn.query(
      "UPDATE pedidos SET status = ? WHERE id = ?",
      [status, id]
    )
    conn.release()

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Pedido não encontrado" })
    }

    res.json({ message: "Pedido atualizado com sucesso", status })
  } catch (error) {
    console.error(error)
    res.status(500).json({ error: "Erro ao atualizar pedido" })
  }
})

// Deletar Pedido
app.delete("/pedidos/:id", async (req, res) => {
  const { id } = req.params

  try {
    const conn = await pool.getConnection()
    const [result] = await conn.query("DELETE FROM pedidos WHERE id = ?", [id])
    conn.release()

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: "Pedido não encontrado" })
    }

    res.status(204).send()
  } catch (error) {
    console.error(error)
    res.status(500).json({ error: "Erro ao deletar pedido" })
  }
})

// Servidor Local
app.listen(3000, () => console.log("Servidor rodando na porta 3000"))

// Exportação para Serverless
module.exports.handler = serverless(app)
