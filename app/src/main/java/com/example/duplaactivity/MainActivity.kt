package com.example.duplaactivity

import android.content.Intent
import android.net.Uri
import android.net.Uri.parse
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.duplaactivity.ui.theme.DuplaActivityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //SharedPreferences => LocalStorage
        val prefs = getSharedPreferences("diakprofil_adatok", MODE_PRIVATE)
        setContent {

            var mentettNev by remember{
                mutableStateOf(
                    prefs.getString("nev", "nincs név megadva") ?: ""
                )
            }
            var nev by remember {
                mutableStateOf(mentettNev)
            }
            var sotetMod by remember {
                mutableStateOf(prefs.getBoolean("sotetMod", false))
            }

            MaterialTheme(
                colorScheme = if(sotetMod){
                    darkColorScheme()
                } else{
                    lightColorScheme()
                }
            ){
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .safeDrawingPadding()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Diákprofil",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        OutlinedTextField(
                            value = nev,
                            onValueChange = {
                                nev = it
                            },
                            label = {
                                Text("Add meg a neved")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                val intent =
                                    Intent(
                                        this@MainActivity,
                                        ProfileActivity::class.java
                                    )
                                intent.putExtra("nev", nev)
                                startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Profil megtekintése")
                        }

                        Button(
                            onClick = {
                                prefs.edit().putString("nev", nev).apply()
                                mentettNev = nev
                                Toast.makeText(this@MainActivity, "Név elmentve",
                                    Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Név elmentése")
                        }

                        OutlinedButton(
                            onClick = {
                                prefs.edit().remove("nev").apply()
                                nev = ""
                                mentettNev= ""
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Név törlése")
                        }

                        HorizontalDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Sötét mód",
                                style = MaterialTheme.typography.titleMedium)
                            Switch(checked = sotetMod,
                                onCheckedChange = {
                                    bekapcsolva ->
                                    sotetMod = bekapcsolva
                                    prefs.edit().putBoolean("sotetMod",
                                        bekapcsolva).apply()
                                })
                        }
                        HorizontalDivider()

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://petrik.hu")
                                )
                                startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Weboldal megnyitása")
                        }
                    }
                }
            }
        }
    }
}