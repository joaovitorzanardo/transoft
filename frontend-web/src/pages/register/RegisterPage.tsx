import { Button, Card, Divider, Step, StepLabel, Stepper, TextField, Typography } from "@mui/material";
import React from "react";
import { useForm, Controller, type SubmitHandler } from "react-hook-form";
import { Link, useNavigate } from "react-router";

interface IFormInputs {
    UserName: string
    UserEmail: string
    UserPass: string
    UserConfimPass: string
    CompanyName: string
    CompanyEmail: string
    CNPJ: string
  }

export default function RegisterPage() {
    const navidate = useNavigate();

    const { handleSubmit, control, trigger } = useForm<IFormInputs>({
        defaultValues: {
            UserName: '',
            UserEmail: '',
            UserPass: '',
            UserConfimPass: '',
            CompanyName: '',
            CompanyEmail: '',
            CNPJ: ''
        },
    });

    const [activeStep, setActiveStep] = React.useState(0);

    const handleNext = () => {
        if (activeStep === 0) {
            trigger(['UserName', 'UserEmail', 'UserPass', 'UserConfimPass'])
            .then(isValid => {
                if (isValid) setActiveStep((prevActiveStep) => prevActiveStep + 1);
            });
        }
    }

    const handlePrevious = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    }

    const registerAccount = () => {
        navidate('/dashboard');
    }

    const onSubmit: SubmitHandler<IFormInputs> = (data) => console.log(data)

    return (
        <Card variant="outlined" style={{display: 'flex', flexDirection: 'column', gap: '16px', padding: '16px', maxWidth: '400px', margin: 'auto', marginTop: '100px'}}>
            <form onSubmit={handleSubmit(onSubmit)}>
                {activeStep === 0 ? (
                    <>
                        <Typography variant="h5" component="h2" align="center">Criar Conta</Typography>
                        <Typography variant="subtitle2" align="center">Preencha os dados para começar</Typography>
                        <Stepper activeStep={activeStep}>
                            <Step key="usuario">
                                <StepLabel>Usuário</StepLabel>
                            </Step>
                            <Step key="empresa">
                                <StepLabel>Empresa</StepLabel>
                            </Step>
                        </Stepper>
                        <Controller 
                            name="UserName"
                            control={control}
                            rules={{ required: "O nome deve ser informado" }}
                            render={({ field, fieldState }) => <TextField label="Nome" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field}/>}
                        />
                        <Controller 
                            name="UserEmail"
                            control={control}
                            rules={{ required: "O email deve ser informado" }}
                            render={({ field, fieldState }) => <TextField label="Email" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field}/>}
                        />
                        <Controller 
                            name="UserPass"
                            control={control}
                            rules={{ required: "A senha deve ser informada" }}
                            render={({ field, fieldState }) => <TextField label="Senha" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" type="password" {...field}/>}
                        />
                        <Controller 
                            name="UserConfimPass"
                            control={control}
                            rules={{ required: "A senha deve ser confirmada" }}
                            render={({ field, fieldState }) => <TextField label="Confirmar Senha" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" type="password" {...field}/>}
                        />
                        <Button variant="contained" onClick={handleNext}>Continuar</Button>
                    </>
                ) : (
                    <>
                        <Typography variant="h5" component="h2" align="center">Registrar Empresa</Typography>
                        <Typography variant="subtitle2" align="center">Preencha os dados da sua emrpesa para continuar</Typography>
                        <Stepper activeStep={activeStep}>
                            <Step key="usuario">
                                <StepLabel>Usuário</StepLabel>
                            </Step>
                            <Step key="empresa">
                                <StepLabel>Empresa</StepLabel>
                            </Step>
                        </Stepper>
                        <Controller 
                            name="CompanyName"
                            control={control}
                            rules={{ required: true }}
                            render={({ field, fieldState }) => <TextField label="Nome" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field}/>}
                        />
                        <Controller 
                            name="CompanyEmail"
                            control={control}
                            rules={{ required: true }}
                            render={({ field, fieldState }) => <TextField label="Email" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field}/>}
                        />
                        <Controller 
                            name="CNPJ"
                            control={control}
                            rules={{ required: true }}
                            render={({ field, fieldState }) => <TextField label="CNPJ" error={!!fieldState.error} helperText={fieldState.error?.message} variant="outlined" {...field}/>}
                        />
                        <Button variant="contained" onClick={registerAccount} type="submit">Finalizar Cadastro</Button>
                        <Button variant="outlined" onClick={handlePrevious}>Voltar</Button>
                    </>
                )}
            </form>
            <Divider />
            <Link to="/login" style={{textAlign: 'center'}}>Já possui uma conta? Entre aqui</Link>
        </Card>
        
    );
}