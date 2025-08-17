/* ============================================
 * Disponibilidad (Admin) - JS estático
 * Ruta esperada: /admin/disponibilidad.html
 * Endpoints REST esperados:
 *   GET  /api/mesas
 *   GET  /api/disponibilidad?mesaId=&fecha=YYYY-MM-DD&duracionMin=180
 * ============================================ */

const API = {
  mesas: "/api/mesas",
  disponibilidad: "/api/disponibilidad"
};

// --------- DOM ---------
const $mesaSelect    = document.getElementById("mesaSelect");
const $fechaInput    = document.getElementById("fechaInput");
const $duracionInput = document.getElementById("duracionInput");
const $buscarBtn     = document.getElementById("buscarBtn");
const $slotsGrid     = document.getElementById("slotsGrid");

// --------- Init ---------
(async function init() {
  // fecha hoy por defecto
  if ($fechaInput) {
    const hoy = new Date();
    $fechaInput.value = hoy.toISOString().slice(0, 10);
  }

  await cargarMesas();
  await buscar();

  // listeners
  $buscarBtn?.addEventListener("click", buscar);
  $mesaSelect?.addEventListener("change", buscar);
  $fechaInput?.addEventListener("change", buscar);
  $duracionInput?.addEventListener("change", buscar);
})();

// --------- Data loads ---------
async function cargarMesas() {
  if (!$mesaSelect) return;

  setSelectLoading($mesaSelect, "Cargando mesas…");

  try {
    const res = await fetch(API.mesas, { headers: { Accept: "application/json" } });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const mesas = await res.json();

    if (!Array.isArray(mesas) || mesas.length === 0) {
      $mesaSelect.innerHTML = `<option value="">(sin mesas habilitadas)</option>`;
      return;
    }

    $mesaSelect.innerHTML = mesas
      .map(
        (m) =>
          `<option value="${m.id}">
            ${escapeHtml(m.nombre ?? `Mesa ${m.id}`)}${m.capacidad ? ` (${m.capacidad} pax)` : ""}
          </option>`
      )
      .join("");
  } catch (err) {
    console.error("Error cargando mesas:", err);
    $mesaSelect.innerHTML = `<option value="">Error cargando mesas</option>`;
  }
}

async function buscar() {
  if (!$slotsGrid) return;

  const mesaId = $mesaSelect?.value;
  const fecha = $fechaInput?.value;
  const duracion = Number($duracionInput?.value || 180);

  if (!mesaId || !fecha) {
    paintMessage("Seleccione mesa y fecha para consultar.");
    return;
  }

  paintMessage("Buscando disponibilidad…", "secondary");

  try {
    const url = new URL(API.disponibilidad, window.location.origin);
    url.searchParams.set("mesaId", String(mesaId));
    url.searchParams.set("fecha", fecha);
    url.searchParams.set("duracionMin", String(duracion));

    const res = await fetch(url, { headers: { Accept: "application/json" } });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const slots = await res.json();

    renderSlots(slots);
  } catch (err) {
    console.error("Error consultando disponibilidad:", err);
    paintMessage("No se pudo obtener la disponibilidad.", "danger");
  }
}

// --------- Render ---------
function renderSlots(slots) {
  if (!$slotsGrid) return;

  if (!Array.isArray(slots) || slots.length === 0) {
    paintMessage("No hay horarios para los parámetros seleccionados.", "warning");
    return;
  }

  const frag = document.createDocumentFragment();

  slots.forEach((s) => {
    const disponible = !!s.disponible;
    const motivo = s.motivo || (disponible ? "Disponible" : "Reservado");

    const claseCard = disponible
      ? "slot"
      : motivo === "Bloqueado"
      ? "slot bloqueado"
      : "slot reservado";

    const claseBadge = disponible
      ? "bg-success"
      : motivo === "Bloqueado"
      ? "bg-warning text-dark"
      : "bg-danger";

    const col = document.createElement("div");
    col.className = "col-12 col-md-6 col-lg-4";
    col.innerHTML = `
      <div class="${claseCard}" style="border-radius:12px;padding:.9rem 1rem;border:1px solid #dbe5db;background:#e9f7ef">
        <div class="d-flex justify-content-between align-items-center">
          <div><strong>${toHHMM(s.inicio)} – ${toHHMM(s.fin)}</strong></div>
          <span class="badge ${claseBadge}">${escapeHtml(motivo)}</span>
        </div>
        <div class="mt-2 d-grid">
          ${
            disponible
              ? `<button class="btn btn-outline-success btn-sm"
                     data-accion="crear"
                     data-inicio="${s.inicio}"
                     data-fin="${s.fin}">
                   Reservar / Bloquear
                 </button>`
              : `<button class="btn btn-outline-secondary btn-sm"
                     data-accion="ver"
                     data-id="${s.reservaId ?? ""}" ${s.reservaId ? "" : "disabled"}>
                   Ver / Cancelar
                 </button>`
          }
        </div>
      </div>
    `;
    frag.appendChild(col);
  });

  $slotsGrid.className = "row gy-3";
  $slotsGrid.innerHTML = "";
  $slotsGrid.appendChild(frag);

  // acciones
  $slotsGrid.querySelectorAll("button").forEach((btn) => {
    btn.addEventListener("click", onActionClick);
  });
}

function onActionClick(e) {
  const btn = e.currentTarget;
  const accion = btn.dataset.accion;

  if (accion === "crear") {
    const inicio = btn.dataset.inicio;
    const fin = btn.dataset.fin;
    const mesaTxt = $mesaSelect?.options[$mesaSelect.selectedIndex]?.text || "";
    alert(
      `Crear reserva/bloqueo\n` +
        `Mesa: ${mesaTxt}\n` +
        `Fecha: ${$fechaInput.value}\n` +
        `Inicio: ${toHHMM(inicio)}\n` +
        `Fin: ${toHHMM(fin)}`
    );
    // Aquí conectas tu modal / POST a /api/reservas o /api/bloqueos
  } else if (accion === "ver") {
    const id = btn.dataset.id;
    alert(`Abrir detalle de reserva #${id}`);
    // Aquí conectas GET /api/reservas/{id} o acción de cancelar
  }
}

// --------- Helpers ---------
function paintMessage(texto, tipo = "info") {
  if (!$slotsGrid) return;
  $slotsGrid.className = "row";
  $slotsGrid.innerHTML = `
    <div class="col-12">
      <div class="alert alert-${tipo}">${escapeHtml(texto)}</div>
    </div>
  `;
}

function setSelectLoading(select, texto) {
  if (!select) return;
  select.innerHTML = `<option>${escapeHtml(texto)}</option>`;
}

function toHHMM(hms) {
  // acepta "HH:mm" o "HH:mm:ss" → devuelve "HH:mm"
  if (!hms || typeof hms !== "string") return "";
  return hms.substring(0, 5);
}

function escapeHtml(str) {
  return String(str)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}
