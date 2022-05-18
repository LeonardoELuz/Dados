package br.edu.ifsp.scl.ads. pdm.dados

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.ifsp.scl.ads.pdm.dados.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var geradorRandomico: Random

    private lateinit var settingsActivityLauncher: ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        geradorRandomico = Random(System.currentTimeMillis())

        activityMainBinding.jogarDadoBt.setOnClickListener {

            if (activityMainBinding.resultado2Iv.visibility == View.INVISIBLE) {
                val resultado: Int = geradorRandomico.nextInt(1..6)
                "A face sorteada foi $resultado".also { activityMainBinding.resultadoTv.text = it }
                val nomeImagem = "dice_${resultado}"
                activityMainBinding.resultadoIv.setImageResource(
                    resources.getIdentifier(nomeImagem, "mipmap", packageName)
                )
            }

            if (activityMainBinding.resultado2Iv.visibility == View.VISIBLE) {
                val resultado: Int = geradorRandomico.nextInt(1..6)
                val resultado2: Int = geradorRandomico.nextInt(1..6)

                "A face sorteadas foram $resultado e $resultado2".also { activityMainBinding.resultadoTv.text = it }
                val nomeImagem = "dice_${resultado}"
                val nomeImagem2 = "dice_${resultado2}"

                activityMainBinding.resultadoIv.setImageResource(
                    resources.getIdentifier(nomeImagem, "mipmap", packageName)
                )

                activityMainBinding.resultado2Iv.setImageResource(
                    resources.getIdentifier(nomeImagem2, "mipmap", packageName)
                )

            }

        }

        settingsActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val configuracao: Configuracao? = result.data?.getParcelableExtra<Configuracao>(Intent.EXTRA_USER)
                    if (configuracao != null) {
                        if (configuracao.numeroFaces > 6) {
                            activityMainBinding.resultadoIv.visibility = View.GONE
                            activityMainBinding.resultado2Iv.visibility = View.GONE
                            activityMainBinding.resultadoTv.text = "O número de faces não pode ser maior que 6"

                        }
                        else if (configuracao.numeroDados == 1) {
                            activityMainBinding.resultadoIv.visibility = View.VISIBLE
                            activityMainBinding.resultado2Iv.visibility = View.INVISIBLE
                        }
                        else if(configuracao.numeroDados == 2) {
                            activityMainBinding.resultadoIv.visibility = View.VISIBLE
                            activityMainBinding.resultado2Iv.visibility = View.VISIBLE
                        }

                    }
                }


            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settingMi) {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            settingsActivityLauncher.launch(settingsIntent)
            return true
        }
        return false
    }
}