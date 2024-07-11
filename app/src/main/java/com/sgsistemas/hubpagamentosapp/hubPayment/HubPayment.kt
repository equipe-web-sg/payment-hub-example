package com.sgsistemas.smartposcloud.hubPayment

import android.content.Intent
import android.content.pm.PackageManager

class HubPayment {
    companion object {

        fun payment(intent: Intent, paymentType: String, amount: Double): Intent {
            intent.action = "br.com.getcard.hub.TRANSACTION"
            intent.putExtra("THEME", "ORANGE_THEME")
            intent.putExtra("VALUE", (amount * 100).toInt().toString())

            if (paymentType == EnumSale.CREDIT.toString()) {
                intent.putExtra("TRANSACTION", EnumSale.CREDIT.toString())
                intent.putExtra("MAX_INSTALLMENTS", "8")
                return intent
            }
            intent.putExtra("TRANSACTION", EnumSale.DEBIT.toString())
            intent.putExtra("MAX_INSTALLMENTS", "1")
            return intent
        }

        fun print(intent: Intent, coupon: String): Intent {
            val response = intent.extras?.get("TRANSACTION_DATA") as Map<*, *>
            intent.action = "br.com.getcard.hub.PRINTER_STRING"
            intent.putExtra("DATA", response[coupon].toString().trim())
            return intent
        }

        fun printSecondCopy(intent: Intent, couponClient: String): Intent {
            intent.action = "br.com.getcard.hub.PRINTER_STRING"
            intent.putExtra("DATA", couponClient)
            return intent
        }

        fun refund(intent: Intent, transactionValue: String, controlCode: String): Intent {
            intent.action = "br.com.getcard.hub.REFUND"
            intent.putExtra("THEME", "ORANGE_THEME")
            intent.putExtra("VALUE", transactionValue)
            intent.putExtra("CODE_CONTROL", controlCode)
            return intent
        }

        fun isHubPaymentInstalled(packageName: String, packageManager: PackageManager): Boolean {
            return try {
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }
    }
}