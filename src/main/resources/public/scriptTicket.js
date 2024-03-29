function cambiarEstado(id, nuevoEstado) {
    console.log("Cambiar estado del ticket:", id, "a", nuevoEstado);
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            mostrarTickets();
        }
    };
    xhttp.open("GET", `/cambiarEstado?id=${id}&estado=${nuevoEstado}`, true);
    xhttp.send();
}

function mostrarTickets() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            const tickets = JSON.parse(this.responseText);
            const table = document.createElement("table");
            const header = table.createTHead();
            const row = header.insertRow(0);
            const headers = ["ID", "Dueño de Marca", "URL", "Estado", "Fecha de Creación"];
            headers.forEach(headerText => {
                const th = document.createElement("th");
                const text = document.createTextNode(headerText);
                th.appendChild(text);
                row.appendChild(th);
            });
            const tbody = table.createTBody();
            tickets.forEach(ticket => {
                const tr = tbody.insertRow();
                const data = [ticket.id, ticket.dueñoMarca, ticket.url, ticket.estado, ticket.fechaCreacion];
                data.forEach((cellData, index) => {
                    const td = document.createElement("td");
                    if (index === 3) { 
                        const select = document.createElement("select");
                        const options = ["New", "Aceptado", "Espera de confirmacion", "Monitoreo", "Cerrado"];
                        options.forEach(optionText => {
                            const option = document.createElement("option");
                            option.text = option.value = optionText;
                            if (optionText === cellData) {
                                option.selected = true; 
                            }
                            select.appendChild(option);
                        });
                        select.addEventListener("change", function() {
                            ticket.estado = this.value; 
                            cambiarEstado(ticket.id, this.value); 
                        });
                        td.appendChild(select);
                    } else {
                        const text = document.createTextNode(cellData);
                        td.appendChild(text);
                    }
                    tr.appendChild(td);
                });
            });
            document.getElementById("ticketsTable").innerHTML = ""; // Limpiar contenido previo
            document.getElementById("ticketsTable").appendChild(table);
        }
    };
    xhttp.open("GET", "/obtenerTickets", true);
    xhttp.send();
}

// Llamar a mostrarTickets() cuando la página se carga por primera vez
window.onload = function() {
    mostrarTickets();
};