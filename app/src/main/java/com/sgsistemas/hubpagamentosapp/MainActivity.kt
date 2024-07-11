package com.sgsistemas.hubpagamentosapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.sgsistemas.hubpagamentosapp.databinding.ActivityMainBinding
import com.sgsistemas.smartposcloud.hubPayment.EnumSale
import com.sgsistemas.smartposcloud.hubPayment.HubPayment

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        formaDePagamento()
    }

    private fun formaDePagamento(){
        binding.btnDebito.setOnClickListener {
            pagamento(EnumSale.DEBIT.toString())
        }
        binding.btnCredito.setOnClickListener {
            pagamento(EnumSale.CREDIT.toString())
        }
    }

    private fun pagamento(formaPagamento: String){
        val intent = Intent()
        if(binding.edValor.text.toString().isNotEmpty()) {
            if (!HubPayment.isHubPaymentInstalled("br.com.getcard.hub", packageManager!!)) {
                Toast.makeText(
                    this,
                    "Hub de pagamento não esta instalado no aparelho",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            Toast.makeText(this, "Pagamento em crédito", Toast.LENGTH_SHORT).show()
            someActivityResultLauncher.launch(
                HubPayment.payment(
                    intent,
                    formaPagamento,
                    binding.edValor.text.toString().toDouble()
                )
            )
        }
    }

    private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(result.resultCode, result.data)
    }

    private fun onActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (data.hasExtra("TRANSACTION_DATA")){
                var intent = HubPayment.print(data, "CUPOM_VIA_LOJA")
                someActivityResultLauncher.launch(intent)

                intent = HubPayment.print(data, "CUPOM_VIA_REDUZIDA")
                someActivityResultLauncher.launch(intent)
            }
        }
    }
}