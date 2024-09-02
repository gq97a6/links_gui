package net.hostunit.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import kotlinx.coroutines.launch
import net.hostunit.*
import net.hostunit.API.onFail
import net.hostunit.classes.Address
import net.hostunit.classes.Link

@Composable
fun LinkSlot(
    label: String,
    actionShown: Boolean,
    slot: String,
    slotOnChange: (Int) -> Unit,
    action: Int,
    actionOnChange: (String) -> Unit,
) = Column {

    OutlinedTextField(
        slot,
        actionOnChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.adaptWidth(0.9f, 800.dp).padding(top = 15.dp),
    )

    if (!actionShown) return@Column

    Spacer(Modifier.height(3.dp))

    Row {
        listOf("Karta", "Link", "Schowek").forEachIndexed { i, text ->
            InputChip(
                action == i,
                { slotOnChange(i) },
                { Text(text = text, maxLines = 1, modifier = Modifier.padding(horizontal = 10.dp)) },
                modifier = Modifier.padding(end = 20.dp)
            )
        }
    }
}

enum class EditPageMode() { EDIT, NEW, NONE }

@Composable
fun BoxScope.EditPage(param: String = "") {

    var id by remember { mutableStateOf("") }
    var code by remember { mutableStateOf(param.parseCode()) }
    var direct by remember { mutableStateOf(false) }
    var random by remember { mutableStateOf(true) }
    var newCode by remember { mutableStateOf("") }
    var actions = remember { mutableStateListOf<Int>(0, 0, 0, 0) }
    var slots = remember { mutableStateListOf<String>("", "", "", "") }

    var slotsShown by remember { mutableStateOf(false) }
    var mode by remember { mutableStateOf(EditPageMode.NONE) }

    val scope = rememberCoroutineScope()

    var notiPayload by remember { mutableStateOf("") }
    var notiTrigger by remember { mutableStateOf(false) }

    var popupShown by remember { mutableStateOf(false) }
    var popupText by remember { mutableStateOf("Czy jesteś pewien?") }
    var popupConfirm by remember { mutableStateOf({}) }

    fun onSetupEditMode(address: Address) {
        window.history.replaceState(null, "", "/edit/${address.code}")

        //Unpack address to ui variables
        id = address.id
        code = address.code
        direct = address.direct
        address.links.forEachIndexed { i, link ->
            slots[i] = link.payload
            actions[i] = link.action.ordinal
        }

        mode = EditPageMode.EDIT
        slotsShown = true
    }

    fun onSetupNewMode() {
        window.history.replaceState(null, "", "/edit")

        //Reset UI
        code = ""
        direct = false
        random = true
        newCode = ""
        actions.forEachIndexed { i, _ -> actions[i] = 0 }
        slots.forEachIndexed { i, _ -> slots[i] = "" }

        mode = EditPageMode.NEW
        slotsShown = true
    }

    fun onSetupNoneMode() {
        window.history.replaceState(null, "", "/edit")

        code = ""
        mode = EditPageMode.NONE
        slotsShown = false
    }

    fun onSearch(code: String) {
        scope.launch {
            if (code.isNotEmpty()) API.getAddress(code) {
                onSetupEditMode(it)
            } onFail {
                onSetupNoneMode()
                notiPayload = "Ten adres nie istnieje"
                notiTrigger = !notiTrigger
            }
        }
    }

    fun onPost() {
        popupConfirm = {
            popupShown = false

            if (!random && newCode.isEmpty()) {
                notiPayload = "Kod adresu nie może być pusty"
                notiTrigger = !notiTrigger
            } else scope.launch {
                val address = Address(
                    "",
                    direct
                )

                if (!random) address.code = newCode

                slots.forEachIndexed { i, slot ->
                    address.links.add(Link(slot, Link.Action.values().get(actions[i])))
                }

                API.postAddress(address) {
                    onSetupEditMode(it)
                    notiPayload = "Utworzono nowy adres"
                    notiTrigger = !notiTrigger
                } onFail {
                    notiPayload = "Błąd przetwarzania"
                    notiTrigger = !notiTrigger
                }
            }
        }

        popupShown = true
    }

    fun onPut() {
        popupConfirm = {
            popupShown = false

            scope.launch {
                val address = Address(
                    id,
                    direct,
                    code
                )

                slots.forEachIndexed { i, slot ->
                    address.links.add(Link(slot, Link.Action.values().get(actions[i])))
                }

                API.putAddress(address) {
                    notiPayload = "Zmieniono adres"
                    notiTrigger = !notiTrigger
                } onFail {
                    notiPayload = "Błąd przetwarzania"
                    notiTrigger = !notiTrigger
                }
            }
        }

        popupShown = true
    }

    fun onDelete() {
        popupConfirm = {
            popupShown = false

            scope.launch {
                API.deleteAddress(code) {
                    onSetupNoneMode()
                    notiPayload = "Usunięto adres"
                    notiTrigger = !notiTrigger
                } onFail {
                    notiPayload = "Błąd przetwarzania"
                    notiTrigger = !notiTrigger
                }
            }
        }

        popupShown = true
    }

    if (!cookieExists("token") && !cookieExists("refreshToken")) {
        window.location.href = "/login/$code"
        return
    }

    LaunchedEffect("key") { if (code.isNotEmpty()) onSearch(code) }

    Column(Modifier) {

        Row(
            modifier = Modifier.adaptWidth(0.9f, 800.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isMobile) {
                Logo()
                Spacer(Modifier.width(15.dp))
            }

            Column {
                OutlinedTextField(
                    code,
                    { value ->
                        if (code != value) {
                            onSetupNoneMode()
                        }
                        code = value.parseCode()
                    },
                    label = { Text("Kod") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .onEnter { onSearch(code) },
                )

                Row(Modifier.fillMaxWidth().padding(top = 5.dp)) {
                    FilledTonalButton(
                        modifier = Modifier.weight(1f),
                        enabled = code.isNotEmpty(),
                        onClick = { onSearch(code) }
                    ) {
                        Text(text = "Edytuj", maxLines = 1)
                    }

                    Spacer(Modifier.width(15.dp))

                    Button(
                        modifier = Modifier.weight(1.2f),
                        enabled = mode != EditPageMode.NEW,
                        onClick = { onSetupNewMode() }
                    ) {
                        Text(text = "Stwórz nowy", maxLines = 1)
                    }
                }
            }
        }

        AnimatedVisibility(slotsShown) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.adaptWidth(.9f, 800.dp).height(100.dp).padding(top = 25.dp).offset(x = (-10).dp)
                ) {
                    Checkbox(direct, {
                        direct = it
                        actions[0] = 1
                    })
                    Text("Bezpośredni", color = colorScheme.primary)

                    if (mode == EditPageMode.NEW) {
                        Spacer(Modifier.width(30.dp))

                        Checkbox(random, { random = it })
                        Text("Losowy kod", color = colorScheme.primary)

                        if (!random) OutlinedTextField(
                            newCode,
                            { newCode = it.parseCode() },
                            label = { Text("Kod") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(.7f).padding(start = 30.dp),
                        )
                    }
                }

                repeat(if (direct) 1 else 4) {
                    LinkSlot(
                        "Slot$it",
                        !direct,
                        slots[it],
                        { value -> actions[it] = value },
                        actions[it],
                        { value -> slots[it] = value },
                    )
                }

                Row(Modifier.adaptWidth(.8f, 500.dp).padding(top = 20.dp)) {
                    if (mode == EditPageMode.EDIT) FilledTonalButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onPut() }
                    ) {
                        Text(text = "Potwierdź edycję", maxLines = 1)
                    }

                    if (mode == EditPageMode.NEW) FilledTonalButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onPost() }
                    ) {
                        Text(text = "Potwierdź utworzenie", maxLines = 1)
                    }

                    Spacer(Modifier.width(30.dp))

                    if (mode == EditPageMode.EDIT) Button(
                        modifier = Modifier.weight(1.3f),
                        onClick = { onDelete() }
                    ) {
                        Text(text = "Usuń ten adres", maxLines = 1)
                    }
                }
            }
        }
    }

    Notification(notiPayload, notiTrigger)
    Popup(popupText, popupShown, "Anuluj", { popupShown = false }, "Potwierdź", popupConfirm)
}