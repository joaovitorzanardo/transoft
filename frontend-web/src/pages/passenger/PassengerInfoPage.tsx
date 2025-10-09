import { Box, Breadcrumbs, Button, FormControl, Link, MenuItem, Paper, Select, Stack, Typography, type SelectChangeEvent } from "@mui/material"
import SideMenu from "../../components/SideManu";
import PhoneNumberInput from "../../components/ui/PhoneNumberInput";
import EnderecoPaper from "../../components/ui/EnderecoPaper";
import PersonalInfoPaper from "../../components/ui/PersonalInfoPaper";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, type SubmitHandler } from "react-hook-form";
import React from "react";
import BlockIcon from '@mui/icons-material/Block';

const PassengerForm = z.object({
    name: z.string().nonempty({message: "O nome deve ser informado"}),
    email: z.string().email({message: "Formato do email inválido"}),
    ddd: z.number().positive({message: "O DDD deve ser um número positivo"}),
    number: z.string().nonempty({message: "O número deve ser informado"}),
    schoolId: z.string().nonempty({message: "A matrícula deve ser informada"}),
    cep: z.string().nonempty({message: "O CEP deve ser informado"})
                    .length(8, {message: "O CEP deve ter 8 caracteres"})
                    .regex(/^\d+$/, {message: "O CEP deve conter apenas números"}),
    estado: z.string().nonempty({message: "O estado deve ser informado"}),
    cidade: z.string().nonempty({message: "A cidade deve ser informada"}),
    bairro: z.string().nonempty({message: "O bairro deve ser informado"}),
    endereco: z.string().nonempty({message: "O endereço deve ser informado"}),
    numero: z.number().positive({message: "O número deve ser um número positivo"}),
    complemento: z.string().optional()
});

type IFormInputs = z.infer<typeof PassengerForm>

const rotas = [
    {id: '1', name: 'Rota 1'},
    {id: '2', name: 'Rota 2'},
    {id: '3', name: 'Rota 3'},
];

export default function PassengerInfoPage() {
    const [selectedRoute, setSelectedRoute] = React.useState<string>('Selecionar Rota');
        
    const handleChangeRoute = (event: SelectChangeEvent) => {
        setSelectedRoute(event.target.value as string);
    };

    const { handleSubmit, control, formState: {isValid} } = useForm<IFormInputs>({
        defaultValues: {
            name: '',
            email: '',
            ddd: 54,
            number: '',
            schoolId: '',
            cep: '',
            estado: '',
            cidade: '',
            bairro: '',
            endereco: '',
            numero: 0,
            complemento: ''
        },
        resolver: zodResolver(PassengerForm),
    });

    const onSubmit: SubmitHandler<IFormInputs> = (data) => {        
            if (!isValid) {
                return;
            }
    
            console.log(data);
        }

    return (
        <Stack direction="row" sx={{ backgroundColor: '#F7F9FA'}}>
            <SideMenu />
            <Stack sx={{ padding: '3rem'}}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link underline="hover" color="inherit" href="/passengers">Passageiros</Link>
                    <Typography sx={{ color: 'text.primary' }}>Cadastro</Typography>
                </Breadcrumbs>
                <Box sx={{height: 10}}/>
                <Stack direction="row" sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}} >
                    <Typography variant="h4" >Cadastrar Passageiro</Typography>
                </Stack>
                <Box sx={{height: 20}}/>
                <FormControl onSubmit={handleSubmit(onSubmit)} component="form">
                    <PersonalInfoPaper control={control}/>
                    <Box sx={{height: 30}}/>
                    <PhoneNumberInput control={control}/>
                    <Box sx={{height: 30}}/>
                    <Paper sx={{ padding: 3 }}>
                        <Typography variant="h5" >Rota</Typography>
                        <Select sx={{width: '100%', marginTop: 3}}
                            value={selectedRoute}
                            label="Rota"
                            onChange={handleChangeRoute}
                            >
                            {rotas.map((rota) => (
                                <MenuItem value={rota.name}>
                                    {rota.name}
                                </MenuItem> 
                            ))}
                        </Select>
                    </Paper>
                    <Box sx={{height: 30}}/>
                    <EnderecoPaper control={control}/>
                    <Box sx={{height: 30}}/>
                    <Stack direction="row" justifyContent="flex-start" spacing={2}>
                        <Button variant="contained" color="primary" type="submit">Salvar</Button>
                        <Button variant="outlined" color="error" startIcon={<BlockIcon />}>Desabilitar</Button>
                    </Stack>
                </FormControl>
            </Stack>
        </Stack>
    );
}