package com.example.colenotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.colenotas.ui.theme.ColeNotasTheme
import com.example.colenotas.user.inter.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColeNotasTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    val rutasSinBottomNav = listOf(
        "login",
        "registroEvento",
        "asignarNotas",
        "asignarNotasNotificar",
        "homeAdmin",
        "cursosAdmin",
        "avisosAdmin",
        "registroEventoAdmin",
        "aprobarCurso",
        "mandarAviso",
        "verEventosAdmin",
        "verNotasAdmin",
        "agregarDocente",
        "agregarAlumno"
    )

    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    val mostrarBottomNav = rutasSinBottomNav.none { ruta ->
        currentRoute?.startsWith(ruta) == true
    }

    Scaffold(
        bottomBar = {
            if (mostrarBottomNav) {
                BottomNavDocente(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable(
                route = "homeDocente/{nombre}/{id}",
                arguments = listOf(
                    navArgument("nombre") { type = NavType.StringType },
                    navArgument("id") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                HomeScreen(nombre = nombre, docenteId = id)
            }
            composable("cursos") {
                VentanaCursosScreen(navController = navController)
            }
            composable("calendario") {
                CalendarioScreen()
            }
            composable("perfil") {
                PerfilScreen(navController = navController)
            }
            composable("avisos") {
                AvisosScreen()
            }
            composable("registroEvento") {
                RegistroEventoScreen(navController = navController)
            }
            composable(
                route = "asignarNotas/{cursoId}/{nombreCurso}",
                arguments = listOf(
                    navArgument("cursoId") { type = NavType.IntType },
                    navArgument("nombreCurso") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val cursoId = backStackEntry.arguments?.getInt("cursoId") ?: 0
                val nombreCurso = backStackEntry.arguments?.getString("nombreCurso") ?: ""
                AsignarNotasScreen(cursoId = cursoId, nombreCurso = nombreCurso)
            }
            composable("asignarNotasNotificar") {
                AsignarNotasNotificarScreen()
            }
            composable(
                route = "homeAdmin/{nombre}/{id}",
                arguments = listOf(
                    navArgument("nombre") { type = NavType.StringType },
                    navArgument("id") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                HomeAdminScreen(navController = navController, nombre = nombre)
            }
            composable("cursosAdmin") {
                CursosAdminScreen(navController = navController)
            }
            composable("avisosAdmin") {
                AvisosAdministracionScreen(navController = navController)
            }
            composable("registroEventoAdmin") {
                RegistroEventoAdministracionScreen(navController = navController)
            }
            composable("aprobarCurso") {
                AprobarCursoScreen(navController = navController)
            }
            composable("mandarAviso") {
                MandarAvisoScreen(navController = navController)
            }
            composable("verEventosAdmin") {
                VerEventosAdminScreen(navController = navController)
            }
            composable(
                route = "verNotasAdmin/{cursoId}/{nombreCurso}",
                arguments = listOf(
                    navArgument("cursoId") { type = NavType.IntType },
                    navArgument("nombreCurso") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val cursoId = backStackEntry.arguments?.getInt("cursoId") ?: 0
                val nombreCurso = backStackEntry.arguments?.getString("nombreCurso") ?: ""
                VerNotasAdminScreen(cursoId = cursoId, nombreCurso = nombreCurso)
            }
            composable("agregarDocente") {
                AgregarDocenteScreen(navController = navController)
            }
            composable("agregarAlumno") {
                AgregarAlumnoScreen(navController = navController)
            }
        }
    }
}

@Composable
fun BottomNavDocente(navController: NavController) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentRoute?.startsWith("homeDocente") == true,
            onClick = {
                val nombreEncoded = SesionUsuario.nombre.replace(" ", "%20")
                navController.navigate("homeDocente/$nombreEncoded/${SesionUsuario.id}")
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color.Black,
                indicatorColor = Color.LightGray
            )
        )
        NavigationBarItem(
            selected = currentRoute == "perfil",
            onClick = { navController.navigate("perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color.Black,
                indicatorColor = Color.LightGray
            )
        )
        NavigationBarItem(
            selected = currentRoute == "cursos",
            onClick = { navController.navigate("cursos") },
            icon = { Icon(Icons.Default.Menu, contentDescription = "Cursos") },
            label = { Text("Cursos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color.Black,
                indicatorColor = Color.LightGray
            )
        )
        NavigationBarItem(
            selected = currentRoute == "calendario",
            onClick = { navController.navigate("calendario") },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendario") },
            label = { Text("Calendario") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color.Black,
                indicatorColor = Color.LightGray
            )
        )
        NavigationBarItem(
            selected = currentRoute == "avisos",
            onClick = { navController.navigate("avisos") },
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Avisos") },
            label = { Text("Avisos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Black,
                selectedTextColor = Color.Black,
                indicatorColor = Color.LightGray
            )
        )
    }
}