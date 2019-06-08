<template>
    <div>
        <v-toolbar flat color="white">
            <v-toolbar-title>Cadastro Funcionario</v-toolbar-title>
            <v-divider
                    class="mx-2"
                    inset
                    vertical
            ></v-divider>
            <v-spacer></v-spacer>
            <v-flex xs12 sm6 md4>
                <v-text-field
                        prepend-icon="search"
                        label="Pesquisar"
                        single-line
                        hide-details
                        v-model="search"
                />
            </v-flex>
        </v-toolbar>
        <v-data-table
                :headers="headers"
                :items="employees"
                class="elevation-1"
                :pagination.sync="pagination"
                :rows-per-page-items="rowsPerPageItems"
                :rows-per-page-text="rowsPerPageText"
                :total-items="totalItems"
                :loading="loading"
        >
            <template v-slot:items="props">
                <td>{{ props.item.nome }}</td>
                <td>{{ props.item.sobreNome }}</td>
                <td>{{ props.item.email }}</td>
                <td>{{ props.item.nis }}</td>
                <td class="text-xs-right">
                    <v-tooltip bottom>
                        <v-btn icon class="mx-0" @click.native="editItem(props.item)" slot="activator">
                            <v-icon color="teal">edit</v-icon>
                        </v-btn>
                        <span>Editar</span>
                    </v-tooltip>
                    <v-tooltip bottom>
                        <v-btn icon class="mx-0" @click.native="deleteItem(props.item)" slot="activator">
                            <v-icon color="red">delete</v-icon>
                        </v-btn>
                        <span>Remover</span>
                    </v-tooltip>
                </td>
            </template>
            <v-alert slot="no-results" :value="true" color="info" icon="warning">
                Não foram encontrados resultados para: {{ search }}
            </v-alert>
            <template slot="no-data">
                <v-alert :value="true" color="info" icon="warning">
                    Sem registros
                </v-alert>
            </template>
            <template slot="pageText" slot-scope="{ pageStart, pageStop, itemsLength}">
                Registro {{ pageStart }} - {{ pageStop }} de {{itemsLength}}
            </template>
        </v-data-table>
        <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{on}">
                <v-btn
                        dark
                        fab
                        fixed
                        small
                        color="accent"
                        bottom
                        right
                        v-on="on"
                        @click="dialog = !dialog"
                >
                    <v-icon>add</v-icon>
                </v-btn>
            </template>
            <v-card>
                <v-card-title>
                    <span class="headline">{{ formTitle }}</span>
                </v-card-title>

                <v-card-text>
                    <v-form v-model="valid" ref="form" lazy-validation>
                        <v-container grid-list-md>
                            <v-layout wrap>
                                <v-flex xs12 sm6 md12>
                                    <v-text-field v-model="editedItem.nome"
                                                  label="Nome"
                                                  :rules="[rules.nomeLenght, rules.notEmpty]"
                                                  :counter="30"
                                                  maxlength="30"
                                    ></v-text-field>
                                </v-flex>
                                <v-flex xs12 sm6 md12>
                                    <v-text-field v-model="editedItem.sobreNome"
                                                  label="Sobrenome"
                                                  :counter="50"
                                                  maxlength="50"
                                                  :rules="[rules.sobrenomeLenght]"
                                    ></v-text-field>
                                </v-flex>
                                <v-flex xs12 sm6 md12>
                                    <v-text-field v-model="editedItem.email"
                                                  label="Email"
                                                  :rules="[rules.email]"
                                    ></v-text-field>
                                </v-flex>
                                <v-flex xs12 sm6 md12>
                                    <v-text-field
                                            v-model="editedItem.nis"
                                            label="NIS (PIS)"
                                            mask="###.#####.##-#"
                                            :rules="[rules.nisLenght]">
                                    </v-text-field>
                                </v-flex>
                            </v-layout>
                        </v-container>
                    </v-form>
                </v-card-text>

                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="error"  @click="close">Cancelar</v-btn>
                    <v-btn color="success" @click="save">Salvar</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
        <v-dialog v-model="alertDialog" max-width="290">
            <v-card>
                <v-card-text>{{alertDialogText}}</v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn class="red--text darken-1" flat="flat" @click.native="onCancel">Não</v-btn>
                    <v-btn class="blue--text darken-1" flat="flat" @click.native="onConfirm">Sim</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
        <v-snackbar block
                    :timeout="timeout"
                    :top=true
                    v-model="alert"
                    :color="color">
            {{ text }}
            <v-btn flat class="white--text" @click.native="alert = false">Fechar</v-btn>
        </v-snackbar>
    </div>
</template>

<script>
    export default {
      name: 'crud_funcionario',
      data: function () {
        return {
          valid: false,
          dialog: false,
          search: '',
          rowsPerPageText: 'Registros por pagina',
          loading: true,
          rowsPerPageItems: [
            5,
            15,
            25,
            50,
            100
          ],
          pagination: {
            descending: true
          },
          alertDialog: false,
          alertDialogText: '',
          alertDialogTitle: '',
          alert: false,
          text: '',
          timeout: 6000,
          color: '',
          totalItems: 0,
          headers: [
            {
              text: 'Nome',
              align: 'left',
              value: 'nome',
              sortable: false
            },
            {
              text: 'Sobrenome',
              align: 'left',
              value: 'sobrenome',
              sortable: false
            },
            {
              text: 'Email',
              align: 'left',
              value: 'email',
              sortable: false
            },
              {text: 'NIS (PIS)', align: 'left', value: 'nis', sortable: false},
              {text: '', value: '', sortable: false}
          ],
          employees: [],
          editedIndex: -1,
          editedItem: {
            idFuncionario: 0,
            nome: '',
            sobreNome: '',
            email: '',
            nis: ''
          },
          defaultItem: {
            idFuncionario: 0,
            nome: '',
            sobreNome: '',
            email: '',
            nis: ''
          },
          rules: {
            notEmpty: (value) => {
              return !!value || 'Campo Obrigatorio.'
            },
            nomeLenght: (value) => {
              if (!!value && value.trim().length !== 0 && value.length > 30) {
                return 'Tamanho maximo é 30 caracteres.'
              } else if (!!value && value.trim().length !== 0 && value.length < 2) {
                return 'Tamanho minimo é 2 caracteres.'
              }
              return true
            },
            sobrenomeLenght: (value) => {
              if (!!value && value.trim().length !== 0 && value.length > 30) {
                return 'Tamanho maximo é 50 caracteres.'
              } else if (!!value && value.trim().length !== 0 && value.length < 2) {
                return 'Tamanho minimo é 2 caracteres.'
              }
              return true
            },
            email: (val) => {
              let regex = /^[a-z0-9][a-z0-9-_\\.]+@([a-z]|[a-z0-9]?[a-z0-9-]+[a-z0-9])\.[a-z0-9]{2,10}(?:\.[a-z]{2,10})?$/
              if (!!val && val.trim().length !== 0 && !regex.test(val)) {
                return 'Informe um email valido.'
              }
              return true
            },
            nisLenght: (value) => {
              if (!!value && value.trim().length !== 0 && value.length < 11) {
                return 'NIS (PIS) deve conter 11 digitos.'
              }
              return true
            }
          }
        }
      },
      computed: {
        formTitle () {
          return this.editedIndex === -1 ? 'Novo funcionario' : 'Editar funcionario'
        }
      },
      watch: {
        dialog (val) {
          val || this.close()
        },
        pagination: {
          handler () {
            this.initialize()
          },
          deep: true
        },
        search: {
          handler () {
            this.initialize()
          }
        }
      },
      methods: {
        initialize () {
          this.loading = true
          let {sortBy, descending, page, rowsPerPage} = this.pagination
          let params = {
            term: this.search,
            sortBy,
            descending,
            page,
            rowsPerPage
          }
          this.api.getWithParams('employees', params).then((res) => {
            this.employees = res.data.employees
            if (this.pagination.sortBy) {
              this.employees = this.employees.sort((a, b) => {
                const sortA = a[sortBy]
                const sortB = b[sortBy]

                if (descending) {
                  if (sortA < sortB) return 1
                  if (sortA > sortB) return -1
                  return 0
                } else {
                  if (sortA < sortB) return -1
                  if (sortA > sortB) return 1
                  return 0
                }
              })
            }
            this.totalItems = res.data.total
            this.loading = false
          }, (err) => {
            this.loading = false
            console.log(err)
          })
        },
        editItem (item) {
          this.editedIndex = this.employees.indexOf(item)
          this.editedItem = Object.assign({}, item)
          this.dialog = true
        },
        deleteItem (item) {
          this.openDialog('Deseja remover esse registro?', '', () => {
            this.api.deleteData('employee/' + item.idFuncionario.toString()).then((res) => {
              this.initialize()
              this.alert = true
              this.text = 'Registro removido com sucesso.'
              this.color = 'success'
            }, (err) => {
              this.alert = true
              this.text = 'Não foi possivel remover o registro.'
              this.color = 'error'
              console.log(err)
            })
          })
        },
        close () {
          this.dialog = false
          setTimeout(() => {
            // reseta os erros.
            this.$refs.form.reset()
            this.editedItem = Object.assign({}, this.defaultItem)
            this.editedIndex = -1
          }, 300)
        },
        save () {
          if (this.$refs.form.validate()) {
            let employee = this.editedItem
            if (!employee.idFuncionario) {
              this.api.postData('employee', employee).then((res) => {
                this.initialize()
              }, (err) => {
                this.showMessage(err.response.data)
              })
            } else {
              this.api.putData('employee/' + employee.idFuncionario.toString(), employee).then((res) => {
                this.initialize()
              }, (err) => {
                console.log(err)
              })
            }
            this.close()
            // if (this.editedIndex > -1) {
            //   Object.assign(this.employees[this.editedIndex], this.editedItem)
            // } else {
            //   this.employees.push(this.editedItem)
            // }
          }
        },
        openDialog: function (dialogText, dialogTitle, confirmCallback, canelCallbak) {
          this.alertDialog = true
          this.alertDialogText = dialogText
          this.alertDialogTitle = dialogTitle
          if (confirmCallback) this.confirmCallback = confirmCallback
          if (canelCallbak) this.cancelCallback = canelCallbak
        },
        confirmCallback: function () {
        },
        cancelCallback: function () {
        },
        onConfirm: function () {
          this.alertDialog = false
          this.alertDialogText = ''
          this.alertDialogTitle = ''
          this.confirmCallback()
          this.confirmCallback = () => {
          }
        },
        onCancel: function () {
          this.alertDialog = false
          this.alertDialogText = ''
          this.alertDialogTitle = ''
          this.cancelCallback()
          this.cancelCallback = () => {
          }
          console.log('Cancelled')
        }
      },
      mounted: function () {
        this.$nextTick(() => {
          this.initialize()
        })
      }
    }
</script>

<style scoped>

</style>