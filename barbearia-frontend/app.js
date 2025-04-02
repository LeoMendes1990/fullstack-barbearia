// Configuração da API
const API_BASE_URL = 'http://localhost:8080';

// Cache de elementos DOM
const navLinks = document.querySelectorAll('.nav-links a');
const pages = document.querySelectorAll('.page');

// Navegação
navLinks.forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const targetPage = link.getAttribute('data-page');
        
        // Atualiza classes ativas
        navLinks.forEach(l => l.classList.remove('active'));
        pages.forEach(p => p.classList.remove('active'));
        
        link.classList.add('active');
        document.getElementById(targetPage).classList.add('active');
        
        // Carrega dados da página
        loadPageData(targetPage);
    });
});

// Funções de Modal
function showModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.add('active');

    // Se for o modal de agendamento, carrega clientes e serviços
    if (modalId === 'agendamento-form') {
        loadClientesForSelect();
        loadServicosForSelect();
    }
}

function hideModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.remove('active');
}

// Funções de API
async function fetchApi(endpoint, options = {}) {
    try {
        const response = await fetch(`${API_BASE_URL}/${endpoint}`, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            }
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro na requisição');
        }
        
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        }
        return null;
    } catch (error) {
        console.error('Erro:', error);
        alert(error.message);
        throw error;
    }
}

// Carregamento de dados
async function loadPageData(page) {
    switch (page) {
        case 'agendamentos':
            await loadAgendamentos();
            break;
        case 'clientes':
            await loadClientes();
            break;
        case 'servicos':
            await loadServicos();
            break;
    }
}

// Funções de carregamento para selects
async function loadClientesForSelect() {
    const clientes = await fetchApi('clientes');
    const select = document.getElementById('agendamento-cliente');
    select.innerHTML = '<option value="">Selecione um cliente</option>';
    
    clientes?.forEach(cliente => {
        const option = document.createElement('option');
        option.value = cliente.id;
        option.textContent = cliente.nome;
        select.appendChild(option);
    });
}

async function loadServicosForSelect() {
    const servicos = await fetchApi('servicos');
    const select = document.getElementById('agendamento-servico');
    select.innerHTML = '<option value="">Selecione um serviço</option>';
    
    servicos?.forEach(servico => {
        const option = document.createElement('option');
        option.value = servico.id;
        option.textContent = `${servico.nome} - R$ ${servico.preco.toFixed(2)}`;
        select.appendChild(option);
    });
}

// Funções de Agendamentos
async function loadAgendamentos() {
    const agendamentos = await fetchApi('agendamentos');
    const tbody = document.querySelector('#agendamentos-table tbody');
    tbody.innerHTML = '';
    
    agendamentos?.forEach(agendamento => {
        const row = document.createElement('tr');
        const dataHora = new Date(agendamento.dataHora);
        
        row.innerHTML = `
            <td>${dataHora.toLocaleDateString()} ${dataHora.toLocaleTimeString()}</td>
            <td>${agendamento.cliente.nome}</td>
            <td>${agendamento.servico.nome}</td>
            <td>
                <button class="btn-secondary" onclick="deleteAgendamento(${agendamento.id})">Excluir</button>
            </td>
        `;
        
        tbody.appendChild(row);
    });
}

// Funções de Clientes
async function loadClientes() {
    const clientes = await fetchApi('clientes');
    const tbody = document.querySelector('#clientes-table tbody');
    tbody.innerHTML = '';
    
    clientes?.forEach(cliente => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${cliente.nome}</td>
            <td>${cliente.email}</td>
            <td>${cliente.telefone}</td>
            <td>
                <button class="btn-secondary" onclick="deleteCliente(${cliente.id})">Excluir</button>
            </td>
        `;
        
        tbody.appendChild(row);
    });
}

// Funções de Serviços
async function loadServicos() {
    const servicos = await fetchApi('servicos');
    const tbody = document.querySelector('#servicos-table tbody');
    tbody.innerHTML = '';
    
    servicos?.forEach(servico => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${servico.nome}</td>
            <td>R$ ${servico.preco.toFixed(2)}</td>
            <td>
                <button class="btn-secondary" onclick="deleteServico(${servico.id})">Excluir</button>
            </td>
        `;
        
        tbody.appendChild(row);
    });
}

// Event Listeners para formulários
document.getElementById('form-agendamento').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const clienteId = document.getElementById('agendamento-cliente').value;
    const servicoId = document.getElementById('agendamento-servico').value;
    const data = document.getElementById('agendamento-data').value;
    const hora = document.getElementById('agendamento-hora').value;

    const agendamentoData = {
        dataHora: `${data}T${hora}:00`,
        cliente: { id: parseInt(clienteId) },
        servico: { id: parseInt(servicoId) }
    };
    
    try {
        await fetchApi('agendamentos', {
            method: 'POST',
            body: JSON.stringify(agendamentoData)
        });
        
        hideModal('agendamento-form');
        loadAgendamentos();
    } catch (error) {
        // Erro já tratado pelo fetchApi
    }
});

document.getElementById('form-cliente').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const data = {
        nome: document.getElementById('cliente-nome').value,
        email: document.getElementById('cliente-email').value,
        telefone: document.getElementById('cliente-telefone').value
    };
    
    try {
        await fetchApi('clientes', {
            method: 'POST',
            body: JSON.stringify(data)
        });
        
        hideModal('cliente-form');
        loadClientes();
    } catch (error) {
        // Erro já tratado pelo fetchApi
    }
});

document.getElementById('form-servico').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const data = {
        nome: document.getElementById('servico-nome').value,
        preco: parseFloat(document.getElementById('servico-preco').value)
    };
    
    try {
        await fetchApi('servicos', {
            method: 'POST',
            body: JSON.stringify(data)
        });
        
        hideModal('servico-form');
        loadServicos();
    } catch (error) {
        // Erro já tratado pelo fetchApi
    }
});

// Funções de deleção
async function deleteAgendamento(id) {
    if (confirm('Deseja realmente cancelar este agendamento?')) {
        try {
            await fetchApi(`agendamentos/${id}`, { method: 'DELETE' });
            loadAgendamentos();
        } catch (error) {
            // Erro já tratado pelo fetchApi
        }
    }
}

async function deleteCliente(id) {
    if (confirm('Deseja realmente excluir este cliente?')) {
        try {
            await fetchApi(`clientes/${id}`, { method: 'DELETE' });
            loadClientes();
        } catch (error) {
            // Erro já tratado pelo fetchApi
        }
    }
}

async function deleteServico(id) {
    if (confirm('Deseja realmente excluir este serviço?')) {
        try {
            await fetchApi(`servicos/${id}`, { method: 'DELETE' });
            loadServicos();
        } catch (error) {
            // Erro já tratado pelo fetchApi
        }
    }
}

// Carrega dados iniciais
document.addEventListener('DOMContentLoaded', () => {
    loadPageData('agendamentos');
});
