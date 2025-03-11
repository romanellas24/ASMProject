type Account { owner: string id: int }

interface BankInterface {
RequestResponse:
    // createUser( UserWithUsername )( void ) throws UserExists( string ),
    createAccount( Account )( void ),
    default( undefined )( string )
}

service App {
    execution: sequential

    inputPort Web {
        location: "socket://localhost:8100"
        protocol: http {
            format = "json"
            osc << {
                createUser << {
                    template = "/api/account"
                    method = "POST"
                    statusCodes = 201
                    statusCodes.TypeMismatch = 400
                    statusCodes.UserExists = 400
                    response.headers -> responseHeaders
                }
            }
            default = "default"
        }
        interfaces: BankInterface
    }

    init {
        global.accounts << {
            one << {
                owner = "romanellaz", id = 1
            }
            two << {
                owner = "Acmeat", id = 2
            }
        }
    }

    main {
        /*
        [ createUser( request )( ) {
            if( is_defined( global.users.(request.username) ) ) {
                throw( UserExists, request.username )
            } else {
                global.users.(request.username) << request
                undef( global.users.(request.username).username )
                responseHeaders.Location = "/api/user/" + request.username
            }
        } ]
        */
        [ createAccount( request )( ) {
            global.accounts.(request.id) << request
            responseHeaders.Location = "/api/account/" + request.id
        } ]

        [ default( )( "API listens under /api/account/..." ) ]
    }
}
